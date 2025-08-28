package org.singularux.music.feature.saverestoreplaybackstate

import android.content.Context
import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.datastore.core.DataStore
import androidx.hilt.work.HiltWorker
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.withContext
import org.singularux.music.core.playback.MusicControllerFacade
import org.singularux.music.data.playbackstate.position.Position
import org.singularux.music.data.playbackstate.timeline.repository.SavedMediaItemRepository

@HiltWorker
class RestorePlaybackStateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val savedMediaItemRepository: SavedMediaItemRepository,
    private val positionDataStore: DataStore<Position>
) : CoroutineWorker(
    appContext = appContext,
    params = params
) {

    override suspend fun doWork(): Result {
        // Create controller
        val musicControllerFacade = MusicControllerFacade(
            context = applicationContext,
            coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        )
        val mediaController = musicControllerFacade.mediaControllerDeferred.await()
        // Retrieve data
        val mediaItemList = savedMediaItemRepository.getAll().map {
            val trackItemUri = "${MediaStore.Audio.Media.EXTERNAL_CONTENT_URI}/${it.itemId}".toUri()
            val extras = bundleOf("playing_from" to it.playingFrom)
            if (it.addedByUser) extras.putString("added_by", "user")
            val mediaMetadata = MediaMetadata.Builder()
                .setTitle(it.title)
                .setArtist(it.artist)
                .setDurationMs(it.duration.inWholeMilliseconds)
                .setArtworkUri(it.artworkUri)
                .setExtras(extras)
                .build()
            MediaItem.Builder()
                .setMediaId("${it.itemId}")
                .setUri(trackItemUri)
                .setMediaMetadata(mediaMetadata)
                .build()
        }
        val position = positionDataStore.data.last()
        // Restore only if the list is not empty
        if (mediaItemList.isNotEmpty()) {
            withContext(Dispatchers.Main) {
                mediaController.setMediaItems(mediaItemList)
                mediaController.seekTo(position.index, position.positionMs)
            }
        }
        // Release controller and return success to avoid retry
        mediaController.release()
        return Result.success()
    }

}