package org.singularux.music.feature.playback.presentation

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import org.singularux.music.feature.playback.foreground.MusicControllerFacade
import org.singularux.music.feature.playback.model.PlaybackProgress
import javax.inject.Inject
import kotlin.math.max

class ListenPlaybackProgressUseCase @Inject constructor(
    private val musicControllerFacade: MusicControllerFacade
) {

    companion object {
        private const val TAG = "ListenPlaybackProgressUseCase"
        private const val DELAY = 300L
    }

    operator fun invoke(): Flow<PlaybackProgress> = channelFlow {
        // Get MediaController and every DELAY ms check for the progress and send to the listeners
        val mediaController = try {
            musicControllerFacade.mediaControllerDeferred.await()
        } catch (e: Exception) {
            Log.e(TAG, "Cannot get MediaController instance", e)
            return@channelFlow
        }
        while (true) {
            val progress = withContext(Dispatchers.Main) {
                val currentPosition = if (mediaController.currentMediaItem != null) {
                    mediaController.currentPosition.toDouble()
                } else {
                    0.0
                }
                val totalDuration = max(1L, mediaController.contentDuration).toDouble()
                (currentPosition / totalDuration).toFloat()
            }
            Log.v(TAG, "Sending playback progress: $progress")
            send(PlaybackProgress(progress = progress))
            delay(DELAY)
        }
    }

}