package org.singularux.music.feature.tracklist.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.singularux.music.feature.tracklist.ui.TrackItemData
import javax.inject.Inject

class ListenTrackListUseCase @Inject constructor() {

    operator fun invoke(): Flow<List<TrackItemData>> {
        return flow {
            emit(value = emptyList())
        }
    }

}