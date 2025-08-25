package org.singularux.music.feature.tracklist.domain

import android.util.Log
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import org.singularux.music.core.playback.MusicControllerFacade
import org.singularux.music.feature.tracklist.model.PlaybackMetadata
import javax.inject.Inject

class ListenPlaybackMetadataUseCase @Inject constructor(
    private val musicControllerFacade: MusicControllerFacade
) {

    companion object {
        private const val TAG = "ListenPlaybackMetadataUseCase"
    }

    operator fun invoke(): Flow<PlaybackMetadata?> = callbackFlow {
        val mediaController = musicControllerFacade.mediaControllerDeferred.await()
        val listener = object : Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) = update()
            fun update() {
                val playbackMetadata = if (mediaController.mediaMetadata != MediaMetadata.EMPTY) {
                    PlaybackMetadata(
                        title = mediaController.mediaMetadata.title?.toString() ?: "",
                        artistName = mediaController.mediaMetadata.artist?.toString(),
                        artworkUri = mediaController.mediaMetadata.artworkUri,
                        playingFromExtra = mediaController.mediaMetadata.extras?.getString("playing_from")
                    )
                } else {
                    null
                }
                trySend(element = playbackMetadata)
                    .onSuccess { Log.d(TAG, "Sent $playbackMetadata") }
                    .onFailure { Log.e(TAG, "Cannot send PlaybackMetadata", it) }
            }
        }
        mediaController.addListener(listener)
        withContext(Dispatchers.Main) { listener.update() }
        awaitClose { musicControllerFacade.removeListener(listener) }
    }

}