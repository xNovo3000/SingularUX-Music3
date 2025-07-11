package org.singularux.music.feature.playback.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.singularux.music.feature.playback.foreground.MusicControllerFacade
import org.singularux.music.feature.playback.presentation.ListenPlaybackInfoUseCase
import org.singularux.music.feature.playback.presentation.ListenPlaybackProgressUseCase
import org.singularux.music.feature.playback.presentation.ListenPlaybackStateUseCase
import org.singularux.music.feature.playback.ui.PlaybackBarState
import javax.inject.Inject

@HiltViewModel
class PlaybackBarViewModel @Inject constructor(
    listenPlaybackInfoUseCase: ListenPlaybackInfoUseCase,
    listenPlaybackProgressUseCase: ListenPlaybackProgressUseCase,
    listenPlaybackStateUseCase: ListenPlaybackStateUseCase,
    private val musicControllerFacade: MusicControllerFacade
) : ViewModel() {

    companion object {
        val EMPTY_PLAYBACK_BAR_STATE = PlaybackBarState(
            title = null,
            artistsName = null,
            artworkUri = null,
            isPlaying = false,
            progress = 0.0F
        )
    }

    val playbackBarState = combine(
        listenPlaybackInfoUseCase(),
        listenPlaybackProgressUseCase(),
        listenPlaybackStateUseCase()
    ) { maybePlaybackInfo, playbackProgress, playbackState ->
        PlaybackBarState(
            title = maybePlaybackInfo?.title,
            artistsName = maybePlaybackInfo?.artistsName,
            artworkUri = maybePlaybackInfo?.artworkUri,
            isPlaying = playbackState.isPlaying,
            progress = playbackProgress.progress
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EMPTY_PLAYBACK_BAR_STATE)

    fun play() {
        musicControllerFacade.mediaController?.play()
    }

    fun pause() {
        musicControllerFacade.mediaController?.pause()
    }

}