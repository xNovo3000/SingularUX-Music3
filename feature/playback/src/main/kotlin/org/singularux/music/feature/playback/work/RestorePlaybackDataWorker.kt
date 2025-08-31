package org.singularux.music.feature.playback.work

import android.content.Context
import android.provider.MediaStore
import android.util.Log
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.singularux.music.data.playbackstate.position.Position
import org.singularux.music.data.playbackstate.timeline.repository.SavedMediaItemRepository
import org.singularux.music.feature.playback.foreground.MusicControllerFacade

@HiltWorker
class RestorePlaybackDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val positionDataStore: DataStore<Position>,
    private val savedMediaItemRepository: SavedMediaItemRepository
) : CoroutineWorker(
    appContext = appContext,
    params = params
) {

    companion object {
        const val TAG = "RestorePlaybackStateWorker"
    }

    override suspend fun doWork(): Result {
        // Retrieve position and saved items. Abort if empty
        val position = positionDataStore.data.firstOrNull() ?: Position(index = 0, positionMs = 0)
        val mediaItems = savedMediaItemRepository.getAll()
            .map {
                val trackItemUri = "${MediaStore.Audio.Media.EXTERNAL_CONTENT_URI}/${it.itemId}".toUri()
                val extras = bundleOf("playing_from" to "$${it.playingFrom}/${it.id}")
                if (it.addedByUser) {
                    extras.putString("added_by", "user")
                }
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
        if (mediaItems.isEmpty()) {
            Log.d(TAG, "No data is currently saved. Aborting")
            return Result.failure()
        }
        Log.d(TAG, "Retrieved ${mediaItems.size} media items with position at index ${position.index}")
        // Get controller
        val musicControllerFacade = MusicControllerFacade(
            context = applicationContext,
            coroutineScope = CoroutineScope(context = Dispatchers.Default)
        )
        val mediaController = musicControllerFacade.mediaControllerDeferred.await()
        Log.d(TAG, "Retrieved MediaController")
        // Update the current MediaItems and the seeking position
        withContext(Dispatchers.Main) {
            mediaController.setMediaItems(mediaItems)
            mediaController.seekTo(position.index, position.positionMs)
            mediaController.prepare()
        }
        Log.d(TAG, "Updated media items and seek position")
        // Release controller and return success
        withContext(Dispatchers.Main) { mediaController.release() }
        Log.d(TAG, "Released MediaController")
        return Result.success()
    }

}