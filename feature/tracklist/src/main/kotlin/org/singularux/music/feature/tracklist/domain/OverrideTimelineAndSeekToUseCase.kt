package org.singularux.music.feature.tracklist.domain

import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.singularux.music.core.playback.MusicControllerFacade
import org.singularux.music.feature.tracklist.ui.TrackItemData
import javax.inject.Inject

class OverrideTimelineAndSeekToUseCase @Inject constructor(
    private val musicControllerFacade: MusicControllerFacade
) {

    companion object {
        private const val TAG = "OverrideTimelineAndSeekToUseCase"
    }

    suspend operator fun invoke(
        tagPrefix: String,
        newTracks: List<TrackItemData>,
        index: Int,
    ) = withContext(Dispatchers.Default) {
        if (index > newTracks.size) {
            Log.e(TAG, "Received larger index that tracks list size")
            return@withContext
        }
        val mediaItemList = newTracks.map { trackItemData ->
            val trackItemUri = "${MediaStore.Audio.Media.EXTERNAL_CONTENT_URI}/${trackItemData.id}".toUri()
            val extras = bundleOf("playing_from" to "$tagPrefix/${trackItemData.id}")
            val mediaMetadata = MediaMetadata.Builder()
                .setTitle(trackItemData.title)
                .setArtist(trackItemData.artistName)
                .setDurationMs(trackItemData.duration.inWholeMilliseconds)
                .setArtworkUri(trackItemData.artworkUri)
                .setExtras(extras)
                .build()
            MediaItem.Builder()
                .setMediaId("${trackItemData.id}")
                .setUri(trackItemUri)
                .setMediaMetadata(mediaMetadata)
                .build()
        }
        withContext(Dispatchers.Main) {
            musicControllerFacade.mediaController?.let {
                it.clearMediaItems()
                it.addMediaItems(mediaItemList)
                it.seekTo(index, 0)
                it.play()
            }
        }
    }

}