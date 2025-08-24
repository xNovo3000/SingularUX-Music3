package org.singularux.music.feature.tracklist.domain

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import org.singularux.music.core.playback.MusicControllerFacade
import org.singularux.music.feature.tracklist.model.PlaybackProgress
import javax.inject.Inject
import kotlin.math.max

class ListenPlaybackProgressUseCase @Inject constructor(
    private val musicControllerFacade: MusicControllerFacade
) {

    companion object {
        private const val TAG = "ListenPlaybackProgressUseCase"
        private const val UPDATE_DELAY_MS = 300L
    }

    operator fun invoke(): Flow<PlaybackProgress> = channelFlow {
        val mediaController = musicControllerFacade.mediaControllerDeferred.await()
        while (true) {
            val (current, total) = withContext(Dispatchers.Main) {
                Pair(mediaController.currentPosition, max(mediaController.contentDuration, 0))
            }
            val progress = (current.toDouble() / total.toDouble()).toFloat()
            trySend(PlaybackProgress(progress = progress))
                .onSuccess { Log.v(TAG, "Sent $it") }
                .onFailure { Log.e(TAG, "Cannot send progress", it) }
            delay(UPDATE_DELAY_MS)
        }
    }

}