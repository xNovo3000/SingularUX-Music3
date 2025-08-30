package org.singularux.music.feature.playback.domain

import android.util.Log
import androidx.media3.common.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.singularux.music.core.playback.MusicControllerFacade
import org.singularux.music.feature.playback.model.PlaybackProgress
import javax.inject.Inject
import kotlin.math.max

class ListenPlaybackProgressUseCase @Inject constructor(
    private val musicControllerFacade: MusicControllerFacade
) {

    companion object {
        private const val TAG = "ListenPlaybackProgressUseCase"
        private const val UPDATE_DELAY_MS = 300L
    }

    operator fun invoke(): Flow<PlaybackProgress> = callbackFlow {
        // Wait for MediaController to become available
        val mediaController = musicControllerFacade.mediaControllerDeferred.await()
        // This lambda updates the playback flow ensuring the data is valid
        val updateFunction: (positionMs: Long, totalMs: Long) -> Unit = { positionMs, totalMs ->
            val playbackProgress = PlaybackProgress(
                progress = (positionMs.toDouble() / totalMs.toDouble()).toFloat()
            )
            when (playbackProgress.progress) {
                in 0.0..1.0 -> trySend(element = playbackProgress)
                    .onSuccess { Log.v(TAG, "Sent $playbackProgress") }
                    .onFailure { Log.e(TAG, "Cannot send progress", it) }
                else -> Log.v(TAG, "Cannot send invalid progress $playbackProgress")
            }
        }
        // We must update the playback position in two cases:
        // 1. Every UPDATE_DELAY_MS milliseconds automatically if it is not buffering
        // 2. When a "PositionDiscontinuity" is being launched by the controller
        // Furthermore, cache the current content duration because when a "PositionDiscontinuity"
        // is launched, the player can be buffering and will return 'contentDuration' 0
        val listener = object : Player.Listener {
            var cachedContentDurationMs: Long = 1
            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                updateFunction(newPosition.positionMs, cachedContentDurationMs)
            }
        }
        val scheduledJob = launch(context = Dispatchers.Default) {
            while (true) {
                val (isBuffering, positionMs, totalMs) = withContext(Dispatchers.Main) {
                    Triple(
                        first = mediaController.isLoading,
                        second = mediaController.currentPosition,
                        third = max(mediaController.contentDuration, 1L)
                    )
                }
                if (!isBuffering) {
                    listener.cachedContentDurationMs = totalMs
                    updateFunction(positionMs, totalMs)
                } else {
                    Log.v(TAG, "Buffering, cannot send invalid PlaybackProgress value")
                }
                delay(UPDATE_DELAY_MS)
            }
        }
        // Remove both listener and job on close
        awaitClose {
            musicControllerFacade.removeListener(listener)
            scheduledJob.cancel()
        }
    }

}