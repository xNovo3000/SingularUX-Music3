package org.singularux.music.feature.tracklist.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.singularux.music.data.library.repository.TrackRepository
import org.singularux.music.feature.tracklist.ui.TrackItemData
import javax.inject.Inject

class GetTrackListByNameUseCase @Inject constructor(
    private val trackRepository: TrackRepository
) {

    suspend operator fun invoke(name: String, limit: Int = 20): List<TrackItemData> {
        return withContext(Dispatchers.Default) {
            if (name.isBlank() || limit < 1) {
                return@withContext emptyList()
            }
            trackRepository.getAllByName(name = name, limit = limit)
                .map { trackEntity ->
                    TrackItemData(
                        id = trackEntity.id,
                        title = trackEntity.title,
                        artistName = trackEntity.artistName,
                        duration = trackEntity.duration,
                        artworkUri = trackEntity.artworkUri,
                        isCurrentlyPlaying = false  // TODO: Implement this
                    )
                }
        }
    }

}