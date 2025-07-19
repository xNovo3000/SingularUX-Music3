package org.singularux.music.feature.playback.presentation

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
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

            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) = updatePlaybackInfo()

            fun updatePlaybackInfo() {
                // Always called on main thread, safe to use
                val playbackInfo = if (mediaController.currentMediaItem != null) {
                    PlaybackInfo(
                        mediaId = mediaController.currentMediaItem?.mediaId,
                        title = mediaController.mediaMetadata.title?.toString(),
                        artistsName = mediaController.mediaMetadata.artist?.toString(),
                        artworkUri = mediaController.mediaMetadata.artworkUri
                    )
                } else {
                    null
                }
                trySend(playbackInfo)
                    .onSuccess { Log.d(TAG, "Successfully sent PlaybackInfo $playbackInfo") }
                    .onFailure { Log.e(TAG, "Cannot send PlaybackInfo $playbackInfo", it) }
            }

        }
        withContext(Dispatchers.Main) {
            mediaController.addListener(listener)
        }
        listener.updatePlaybackInfo()
        awaitClose { musicControllerFacade.removeListener(listener) }
    }

}