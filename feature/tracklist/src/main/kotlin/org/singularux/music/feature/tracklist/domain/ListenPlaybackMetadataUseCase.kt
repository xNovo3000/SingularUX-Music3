package org.singularux.music.feature.tracklist.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.singularux.music.feature.tracklist.model.PlaybackMetadata
import javax.inject.Inject

class ListenPlaybackMetadataUseCase @Inject constructor() {

    operator fun invoke(): Flow<PlaybackMetadata> {
        return flow {
            emit(
                value = PlaybackMetadata(
                    title = "In The End",
                    artistName = "Linkin Park",
                    artworkUri = null,
                    isPlaying = true
                )
            )
        }
    }

}