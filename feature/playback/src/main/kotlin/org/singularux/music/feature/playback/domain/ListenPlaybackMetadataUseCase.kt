package org.singularux.music.feature.playback.domain

import android.util.Log
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
import org.singularux.music.feature.playback.model.PlaybackMetadata
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class ListenPlaybackMetadataUseCase @Inject constructor(
    private val musicControllerFacade: MusicControllerFacade
) {

    companion object {
        private const val TAG = "ListenPlaybackMetadataUseCase"
    }

    operator fun invoke(): Flow<PlaybackMetadata?> = callbackFlow {
        // Wait for MediaController to become available
        val mediaController = musicControllerFacade.mediaControllerDeferred.await()
        // We should listen only for media metadata change
        val listener = object : Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) = update()
            fun update() {
                val playbackMetadata = if (mediaController.mediaMetadata != MediaMetadata.EMPTY) {
                    PlaybackMetadata(
                        title = mediaController.mediaMetadata.title?.toString() ?: "",
                        artistName = mediaController.mediaMetadata.artist?.toString(),
                        artworkUri = mediaController.mediaMetadata.artworkUri,
                        duration = (mediaController.mediaMetadata.durationMs ?: 1).milliseconds,
                        playingFrom = mediaController.mediaMetadata.extras?.getString("playing_from")
                    )
                } else {
                    null
                }
                trySend(element = playbackMetadata)
                    .onSuccess { Log.d(TAG, "Sent $playbackMetadata") }
                    .onFailure { Log.e(TAG, "Cannot send PlaybackMetadata", it) }
            }
        }
        // Add listener and force first update
        mediaController.addListener(listener)
        withContext(Dispatchers.Main) { listener.update() }
        // Remove when finished
        awaitClose { musicControllerFacade.removeListener(listener) }
    }

}