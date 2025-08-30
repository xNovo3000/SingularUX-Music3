package org.singularux.music.feature.playback.domain

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
import org.singularux.music.feature.playback.model.PlaybackState
import javax.inject.Inject

class ListenPlaybackStateUseCase @Inject constructor(
    private val musicControllerFacade: MusicControllerFacade
) {

    companion object {
        private const val TAG = "ListenPlaybackStateUseCase"
    }

    operator fun invoke(): Flow<PlaybackState> = callbackFlow {
        // Wait for MediaController to become available
        val mediaController = musicControllerFacade.mediaControllerDeferred.await()
        // We should listen for play/pause button and media item transition
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) = update()
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) = update()
            fun update() {
                val playbackState = PlaybackState(
                    isEnabled = mediaController.currentMediaItem != null,
                    isPlaying = mediaController.isPlaying,
                    hasNext = mediaController.hasNextMediaItem()
                )
                trySend(element = playbackState)
                    .onSuccess { Log.d(TAG, "Sent $playbackState") }
                    .onFailure { Log.e(TAG, "Cannot send PlaybackState", it) }
            }
        }
        // Add listener and force first update
        mediaController.addListener(listener)
        withContext(Dispatchers.Main) { listener.update() }
        // Remove when finished
        awaitClose { musicControllerFacade.removeListener(listener) }
    }

}