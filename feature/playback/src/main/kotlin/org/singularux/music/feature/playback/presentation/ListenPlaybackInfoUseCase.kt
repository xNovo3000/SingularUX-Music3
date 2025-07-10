package org.singularux.music.feature.playback.presentation

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.singularux.music.feature.playback.foreground.MusicControllerFacade
import org.singularux.music.feature.playback.model.PlaybackInfo
import javax.inject.Inject

class ListenPlaybackInfoUseCase @Inject constructor(
    private val musicControllerFacade: MusicControllerFacade
) {

    companion object {
        private const val TAG = "ListenPlaybackInfoUseCase"
    }

    operator fun invoke(): Flow<PlaybackInfo?> = callbackFlow {
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

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) = updatePlaybackInfo()

            fun updatePlaybackInfo() {
                // Always called on main thread, safe to use
                val playbackInfo = if (mediaController.currentMediaItem != null) {
                    val title = mediaController.mediaMetadata.title?.toString() ?: ""
                    val artistsName = mediaController.mediaMetadata.artist?.toString()
                    val artworkUri = mediaController.mediaMetadata.artworkUri
                    PlaybackInfo(
                        title = title,
                        artistsName = artistsName,
                        artworkUri = artworkUri
                    )
                } else {
                    null
                }
                trySend(playbackInfo)
                    .onSuccess { Log.d(TAG, "Successfully sent PlaybackInfo $playbackInfo") }
                    .onFailure { Log.e(TAG, "Cannot send PlaybackInfo $playbackInfo", it) }
            }

        }
        mediaController.addListener(listener)
        listener.updatePlaybackInfo()
        awaitClose { musicControllerFacade.removeListener(listener) }
    }

}