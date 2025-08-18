package org.singularux.music.feature.tracklist.domain

import org.singularux.music.feature.tracklist.ui.TrackItemData
import javax.inject.Inject

class GetTrackListByNameUseCase @Inject constructor() {

    suspend operator fun invoke(value: String): List<TrackItemData> {
        return emptyList()
    }

}