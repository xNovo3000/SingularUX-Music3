package org.singularux.music.feature.home.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.singularux.music.data.library.repository.TrackRepository
import org.singularux.music.feature.home.ui.TrackItemData
import javax.inject.Inject

class GetTrackListByNameUseCase @Inject constructor(
    private val trackRepository: TrackRepository
) {

    suspend operator fun invoke(title: String): List<TrackItemData> {
        val trackEntityList = trackRepository.getAllByTitleLike(title)
        return withContext(Dispatchers.Default) {
            trackEntityList.map { trackEntity ->
                TrackItemData(
                    id = trackEntity.id,
                    title = trackEntity.title,
                    artistsName = trackEntity.artistName,
                    duration = trackEntity.duration,
                    artworkUri = trackEntity.artworkUri,
                    isCurrentlyPlaying = false
                )
            }
        }
    }

}