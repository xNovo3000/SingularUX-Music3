package org.singularux.music.feature.nowplaying.domain

import android.util.Log
import androidx.media3.common.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.singularux.music.core.playback.MusicControllerFacade
import org.singularux.music.feature.nowplaying.model.PlaybackProgress
import javax.inject.Inject
import kotlin.math.max

class ListenPlaybackProgressUseCase @Inject constructor(
    private val musicControllerFacade: MusicControllerFacade
) {

    companion object {
        private const val TAG = "ListenPlaybackProgressUseCase"
        private const val UPDATE_DELAY_MS = 300L
    }

    private var cachedContentDurationMs: Long = 1

    operator fun invoke(): Flow<PlaybackProgress> = channelFlow {
        val mediaController = musicControllerFacade.mediaControllerDeferred.await()
        val updateFunction: (Long, Long) -> Unit = { current: Long, total: Long ->
            val playbackProgress = PlaybackProgress(
                progress = (current.toDouble() / total.toDouble()).toFloat()
            )
            when (playbackProgress.progress) {
                in 0.0..1.0 -> trySend(element = playbackProgress)
                    .onSuccess { Log.v(TAG, "Sent $playbackProgress") }
                    .onFailure { Log.e(TAG, "Cannot send progress", it) }
                else -> Log.v(TAG, "Received invalid progress $playbackProgress")
            }
        }
        val listener = object : Player.Listener {
            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                updateFunction(newPosition.contentPositionMs, cachedContentDurationMs)
            }
        }
        val scheduledJob = launch {
            while (true) {
                val (isLoading, current, total) = withContext(Dispatchers.Main) {
                    Triple(
                        first = mediaController.isLoading,
                        second = mediaController.currentPosition,
                        third = max(mediaController.contentDuration, 1)
                    )
                }
                if (!isLoading) {
                    cachedContentDurationMs = total
                    updateFunction(current, total)
                } else {
                    Log.v(TAG, "Cannot update progress because it is buffering")
                }
                delay(UPDATE_DELAY_MS)
            }
        }
        mediaController.addListener(listener)
        awaitClose {
            musicControllerFacade.removeListener(listener)
            scheduledJob.cancel()
        }
    }

}