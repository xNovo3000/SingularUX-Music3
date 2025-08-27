package org.singularux.music.feature.nowplaying.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.singularux.music.feature.nowplaying.domain.ActionPauseMusicUseCase
import org.singularux.music.feature.nowplaying.domain.ActionPlayMusicUseCase
import org.singularux.music.feature.nowplaying.domain.ActionSeekToMusicUseCase
import org.singularux.music.feature.nowplaying.domain.ActionSkipNextMusicUseCase
import org.singularux.music.feature.nowplaying.domain.ActionSkipPrevMusicUseCase
import org.singularux.music.feature.nowplaying.domain.ListenPlaybackMetadataUseCase
import org.singularux.music.feature.nowplaying.domain.ListenPlaybackProgressUseCase
import org.singularux.music.feature.nowplaying.domain.ListenPlaybackStateUseCase
import org.singularux.music.feature.nowplaying.ui.NowPlayingItemArtworkData
import org.singularux.music.feature.nowplaying.ui.NowPlayingItemButtonsData
import org.singularux.music.feature.nowplaying.ui.NowPlayingItemScrubberData
import org.singularux.music.feature.nowplaying.ui.NowPlayingItemTitleData
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    listenPlaybackMetadataUseCase: ListenPlaybackMetadataUseCase,
    listenPlaybackProgressUseCase: ListenPlaybackProgressUseCase,
    listenPlaybackStateUseCase: ListenPlaybackStateUseCase,
    private val actionPauseMusicUseCase: ActionPauseMusicUseCase,
    private val actionPlayMusicUseCase: ActionPlayMusicUseCase,
    private val actionSkipNextMusicUseCase: ActionSkipNextMusicUseCase,
    private val actionSkipPrevMusicUseCase: ActionSkipPrevMusicUseCase,
    private val actionSeekToMusicUseCase: ActionSeekToMusicUseCase
) : ViewModel() {

    val nowPlayingItemArtworkData = listenPlaybackMetadataUseCase()
        .map { maybePlaybackMetadata ->
            NowPlayingItemArtworkData(artworkUri = maybePlaybackMetadata?.artworkUri)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NowPlayingItemArtworkData(artworkUri = null)
        )

    val nowPlayingItemButtonsData = listenPlaybackStateUseCase()
        .map { playbackState ->
            if (playbackState.isEnabled) {
                NowPlayingItemButtonsData.Playing(
                    isPlaying = playbackState.isPlaying,
                    hasNext = playbackState.hasNext
                )
            } else {
                NowPlayingItemButtonsData.Idle
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NowPlayingItemButtonsData.Idle
        )

    val nowPlayingItemScrubberData = combine(
        listenPlaybackMetadataUseCase(),
        listenPlaybackProgressUseCase()
    ) { maybePlaybackMetadata, playbackProgress ->
        NowPlayingItemScrubberData(
            progress = playbackProgress.progress,
            duration = maybePlaybackMetadata?.duration ?: 1.milliseconds
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NowPlayingItemScrubberData(
            progress = 0.0F,
            duration = 1.milliseconds
        )
    )

    val nowPlayingItemTitleData = listenPlaybackMetadataUseCase()
        .map { maybePlaybackMetadata ->
            if (maybePlaybackMetadata != null) {
                NowPlayingItemTitleData.Playing(
                    title = maybePlaybackMetadata.title,
                    artistName = maybePlaybackMetadata.artistName
                )
            } else {
                NowPlayingItemTitleData.Idle
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NowPlayingItemTitleData.Idle
        )

    fun play() {
        viewModelScope.launch { actionPlayMusicUseCase() }
    }

    fun pause() {
        viewModelScope.launch { actionPauseMusicUseCase() }
    }

    fun skipPrev() {
        viewModelScope.launch { actionSkipPrevMusicUseCase() }
    }

    fun skipNext() {
        viewModelScope.launch { actionSkipNextMusicUseCase() }
    }

    fun seekTo(duration: Duration) {
        viewModelScope.launch { actionSeekToMusicUseCase(duration = duration) }
    }

}