package org.singularux.music.feature.playback.work

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.singularux.music.data.playbackstate.position.Position
import org.singularux.music.data.playbackstate.timeline.repository.SavedMediaItemRepository

@HiltWorker
class RestorePlaybackStateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val positionDataStore: DataStore<Position>,
    private val savedMediaItemRepository: SavedMediaItemRepository
) : CoroutineWorker(
    appContext = appContext,
    params = params
) {

    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }

}