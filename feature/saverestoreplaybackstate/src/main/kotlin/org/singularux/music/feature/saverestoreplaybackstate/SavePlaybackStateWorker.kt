package org.singularux.music.feature.saverestoreplaybackstate

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import org.singularux.music.core.playback.MusicControllerFacade
import org.singularux.music.data.playbackstate.position.Position
import org.singularux.music.data.playbackstate.timeline.entity.SavedMediaItemEntity
import org.singularux.music.data.playbackstate.timeline.repository.SavedMediaItemRepository
import kotlin.time.Duration.Companion.milliseconds

@HiltWorker
class SavePlaybackStateWorker @AssistedInject constructor(
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
        // Delete old values
        savedMediaItemRepository.deleteAll()
        positionDataStore.updateData { it.copy(index = 0, positionMs = 0) }
        // Get SavedMediaItem list and Position only if the timeline is not empty
        val mediaItemCount = withContext(Dispatchers.Main) { mediaController.mediaItemCount }
        if (mediaItemCount > 0) {
            val savedMediaItemList = (0 until mediaItemCount)
                .map { index ->
                    withContext(Dispatchers.Main) { mediaController.getMediaItemAt(index) }
                }
                .map { mediaItem ->
                    SavedMediaItemEntity(
                        id = 0,
                        itemId = mediaItem.mediaId.toLongOrNull() ?: 0L,
                        title = mediaItem.mediaMetadata.title?.toString() ?: "",
                        artist = mediaItem.mediaMetadata.artist?.toString(),
                        duration = (mediaItem.mediaMetadata.durationMs ?: 1).milliseconds,
                        artworkUri = mediaItem.mediaMetadata.artworkUri,
                        playingFrom = mediaItem.mediaMetadata.extras?.getString("playing_from") ?: "",
                        addedByUser = mediaItem.mediaMetadata.extras?.getString("added_by") == "user"
                    )
                }
            val position = withContext(Dispatchers.Main) {
                Position(
                    index = mediaController.currentMediaItemIndex,
                    positionMs = mediaController.currentPosition
                )
            }
            savedMediaItemRepository.insertAll(savedMediaItemList)
            positionDataStore.updateData { position }
        }
        // Release controller and return success to avoid retry
        withContext(Dispatchers.Main) { mediaController.release() }
        return Result.success()
    }

}