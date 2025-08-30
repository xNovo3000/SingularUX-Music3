package org.singularux.music.feature.playback.work

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.singularux.music.data.playbackstate.position.Position
import org.singularux.music.data.playbackstate.timeline.entity.SavedMediaItemEntity
import org.singularux.music.data.playbackstate.timeline.repository.SavedMediaItemRepository
import org.singularux.music.feature.playback.foreground.MusicControllerFacade
import kotlin.math.max
import kotlin.time.Duration.Companion.milliseconds

@HiltWorker
class SavePlaybackDataWorker @AssistedInject constructor(
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
        // Get controller
        val musicControllerFacade = MusicControllerFacade(
            context = applicationContext,
            coroutineScope = CoroutineScope(context = Dispatchers.Default)
        )
        val mediaController = musicControllerFacade.mediaControllerDeferred.await()
        Log.d(TAG, "Retrieved MediaController")
        // Retrieve position and saved items. Abort if empty
        val position = withContext(Dispatchers.Main) {
            Position(
                index = max(mediaController.currentMediaItemIndex, 0),
                positionMs = max(mediaController.currentPosition, 0L)
            )
        }
        val mediaItemCount = withContext(Dispatchers.Main) { mediaController.mediaItemCount }
        val savedMediaItems = (0 until mediaItemCount).map {
            val mediaItem = withContext(Dispatchers.Main) { mediaController.getMediaItemAt(it) }
            SavedMediaItemEntity(
                id = 0,
                itemId = mediaItem.mediaId.toLongOrNull() ?: 0L,
                title = mediaItem.mediaMetadata.title?.toString() ?: "",
                artist = mediaItem.mediaMetadata.artist?.toString() ?: "",
                duration = (mediaItem.mediaMetadata.durationMs ?: 1).milliseconds,
                artworkUri = mediaItem.mediaMetadata.artworkUri,
                playingFrom = mediaItem.mediaMetadata.extras?.getString("playing_from") ?: "",
                addedByUser = mediaItem.mediaMetadata.extras?.getString("added_by") == "user"
            )
        }
        if (savedMediaItems.isEmpty()) {
            Log.d(TAG, "No data is currently in playback. Aborting")
            return Result.failure()
        }
        Log.d(TAG, "Retrieved ${savedMediaItems.size} media items with position at index ${position.index}")
        // Save into position datastore and saved items repository
        positionDataStore.updateData { position }
        savedMediaItemRepository.deleteAll()
        savedMediaItemRepository.insertAll(savedMediaItems = savedMediaItems)
        Log.d(TAG, "Saved media items and position")
        // Release controller and return success
        withContext(Dispatchers.Main) { mediaController.release() }
        Log.d(TAG, "Released MediaController")
        return Result.success()
    }

}