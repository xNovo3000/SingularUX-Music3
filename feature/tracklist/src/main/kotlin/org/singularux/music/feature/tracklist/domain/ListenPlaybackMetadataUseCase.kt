package org.singularux.music.feature.tracklist.domain

import android.util.Log
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.singularux.music.core.playback.MusicControllerFacade
import org.singularux.music.feature.tracklist.model.PlaybackMetadata
import javax.inject.Inject

class ListenPlaybackMetadataUseCase @Inject constructor(
    private val musicControllerFacade: MusicControllerFacade
) {

    companion object {
        private const val TAG = "ListenPlaybackMetadataUseCase"
    }

    operator fun invoke(): Flow<PlaybackMetadata?> {
        return callbackFlow {
            val mediaController = musicControllerFacade.mediaControllerDeferred.await()
            val listener = object : Player.Listener {
                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) = update()
                fun update() {
                    val playbackMetadata = mediaController.currentMediaItem?.let {
                        PlaybackMetadata(
                            title = it.mediaMetadata.title?.toString() ?: "",
                            artistName = it.mediaMetadata.artist?.toString(),
                            artworkUri = it.mediaMetadata.artworkUri,
                            isPlaying = mediaController.isPlaying
                        )
                    }
                    trySend(element = playbackMetadata)
                        .onSuccess { Log.d(TAG, "Sent $it") }
                        .onFailure { Log.e(TAG, "Cannot send PlaybackMetadata", it) }
                }
            }
            mediaController.addListener(listener)
            listener.update()
            awaitClose { musicControllerFacade.removeListener(listener) }
        }
    }

}