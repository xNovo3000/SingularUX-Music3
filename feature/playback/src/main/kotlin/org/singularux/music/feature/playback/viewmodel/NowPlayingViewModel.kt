package org.singularux.music.feature.playback.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.singularux.music.feature.playback.model.PlaybackProgress
import org.singularux.music.feature.playback.model.PlaybackState
import org.singularux.music.feature.playback.presentation.ListenPlaybackInfoUseCase
import org.singularux.music.feature.playback.presentation.ListenPlaybackProgressUseCase
import org.singularux.music.feature.playback.presentation.ListenPlaybackStateUseCase
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    listenPlaybackInfoUseCase: ListenPlaybackInfoUseCase,
    listenPlaybackProgressUseCase: ListenPlaybackProgressUseCase,
    listenPlaybackStateUseCase: ListenPlaybackStateUseCase
) : ViewModel() {

    companion object {
        private val EMPTY_PLAYBACK_PROGRESS = PlaybackProgress(progress = 0.0F)
        private val EMPTY_PLAYBACK_STATE = PlaybackState(
            isReady = false,
            isPlaying = false,
            hasNextItem = false
        )
    }

    val maybePlaybackInfo = listenPlaybackInfoUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val playbackProgress = listenPlaybackProgressUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EMPTY_PLAYBACK_PROGRESS)
    val playbackState = listenPlaybackStateUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EMPTY_PLAYBACK_STATE)

    fun seekTo(positionMs: Long) {

    }

}