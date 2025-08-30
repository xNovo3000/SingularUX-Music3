package org.singularux.music.feature.tracklist.domain

import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.singularux.music.feature.playback.foreground.MusicControllerFacade
import org.singularux.music.feature.tracklist.ui.TrackItemData
import javax.inject.Inject

class TimelineAddToQueueUseCase @Inject constructor(
    private val musicControllerFacade: MusicControllerFacade
) {

    companion object {
        private const val TAG = "TimelineAddToQueueUseCase"
    }

    suspend operator fun invoke(
        tagPrefix: String,
        track: TrackItemData
    ) = withContext(Dispatchers.Default) {
        val mediaController = musicControllerFacade.mediaController
        if (mediaController == null) {
            return@withContext
        }
        // Find the index of the first element that does not contain ("added_by", "user")
        val (mediaItemCount, currentMediaItemIndex) = withContext(Dispatchers.Main) {
            Pair(mediaController.mediaItemCount, mediaController.currentMediaItemIndex)
        }
        var newTrackIndex = currentMediaItemIndex
        for (i in (currentMediaItemIndex + 1) until mediaItemCount) {
            val mediaItemExtras = withContext(Dispatchers.Main) {
                mediaController.getMediaItemAt(i).mediaMetadata.extras
            }
            Log.v(TAG, "Viewing mediaItem $i")
            if (mediaItemExtras == null || mediaItemExtras.getString("added_by") != "user") {
                newTrackIndex = i
                break
            }
        }
        // Add MediaItem to that index
        Log.d(TAG, "Adding $track to queue at index $newTrackIndex")
        val mediaItem = track.let {
            val trackItemUri = "${MediaStore.Audio.Media.EXTERNAL_CONTENT_URI}/${it.id}".toUri()
            val extras = bundleOf(
                "playing_from" to "$tagPrefix/${it.id}",
                "added_by" to "user"
            )
            val mediaMetadata = MediaMetadata.Builder()
                .setTitle(it.title)
                .setArtist(it.artistName)
                .setDurationMs(it.duration.inWholeMilliseconds)
                .setArtworkUri(it.artworkUri)
                .setExtras(extras)
                .build()
            MediaItem.Builder()
                .setMediaId("${it.id}")
                .setUri(trackItemUri)
                .setMediaMetadata(mediaMetadata)
                .build()
        }
        withContext(Dispatchers.Main) {
            mediaController.addMediaItem(newTrackIndex, mediaItem)
        }
    }

}