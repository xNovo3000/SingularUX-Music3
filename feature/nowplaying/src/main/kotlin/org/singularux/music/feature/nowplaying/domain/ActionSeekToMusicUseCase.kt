package org.singularux.music.feature.nowplaying.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.singularux.music.feature.playback.foreground.MusicControllerFacade
import javax.inject.Inject
import kotlin.time.Duration

class ActionSeekToMusicUseCase @Inject constructor(
    private val musicControllerFacade: MusicControllerFacade
) {

    suspend operator fun invoke(duration: Duration) {
        withContext(Dispatchers.Main) {
            musicControllerFacade.mediaController?.seekTo(duration.inWholeMilliseconds)
        }
    }

}