package org.singularux.music.feature.nowplaying.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.singularux.music.feature.playback.foreground.MusicControllerFacade
import javax.inject.Inject

class ActionSkipNextMusicUseCase @Inject constructor(
    private val musicControllerFacade: MusicControllerFacade
) {

    suspend operator fun invoke() {
        withContext(Dispatchers.Main) {
            musicControllerFacade.mediaController?.apply {
                seekToNext()
                play()
            }
        }
    }

}