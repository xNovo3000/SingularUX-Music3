package org.singularux.music.feature.playback.presentation

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import org.singularux.music.feature.playback.foreground.MusicControllerFacade
import org.singularux.music.feature.playback.model.PlaybackInfo
import org.singularux.music.feature.playback.model.PlaybackState
import javax.inject.Inject

class ListenPlaybackStateUseCase @Inject constructor(
    private val musicControllerFacade: MusicControllerFacade
) {

    companion object {
        private const val TAG = "ListenPlaybackInfoUseCase"
    }

    operator fun invoke(): Flow<PlaybackState> = callbackFlow {
        // Get MediaController instance
        val mediaController = try {
            musicControllerFacade.mediaControllerDeferred.await()
        } catch (e: Exception) {
            Log.e(TAG, "Cannot get MediaController instance", e)
            return@callbackFlow
        }
        // Add a listener that waits for playback changes
        // Force first start and on finished unregister the listener
        val listener = object : Player.Listener {

            override fun onPlaybackStateChanged(playbackState: Int) = updatePlaybackState()
            override fun onIsPlayingChanged(isPlaying: Boolean) = updatePlaybackState()
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) = updatePlaybackState()

            fun updatePlaybackState() {
                // Always called on main thread, safe to use
                val playbackState = PlaybackState(
                    isReady = mediaController.currentMediaItem != null,
                    isPlaying = mediaController.isPlaying,
                    hasNextItem = mediaController.hasNextMediaItem()
                )
                trySend(playbackState)
                    .onSuccess { Log.d(TAG, "Successfully sent PlaybackState $playbackState") }
                    .onFailure { Log.e(TAG, "Cannot send PlaybackState $playbackState", it) }
            }

        }
        withContext(Dispatchers.Main) {
            mediaController.addListener(listener)
        }
        listener.updatePlaybackState()
        awaitClose { musicControllerFacade.removeListener(listener) }
    }

}