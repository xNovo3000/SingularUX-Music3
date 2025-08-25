package org.singularux.music.feature.tracklist.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.singularux.music.core.playback.MusicControllerFacade
import javax.inject.Inject

class PauseMusicUseCase @Inject constructor(
    private val musicControllerFacade: MusicControllerFacade
) {

    suspend operator fun invoke() = withContext(Dispatchers.Main) {
        musicControllerFacade.mediaController?.pause()
    }

}