package org.singularux.music.feature.tracklist.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.singularux.music.feature.tracklist.model.PlaybackProgress
import javax.inject.Inject

class ListenPlaybackProgressUseCase @Inject constructor() {

    operator fun invoke(): Flow<PlaybackProgress> {
        return flow {
            emit(value = PlaybackProgress(0.0F))
        }
    }

}