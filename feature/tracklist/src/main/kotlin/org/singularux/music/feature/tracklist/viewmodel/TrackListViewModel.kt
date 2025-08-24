package org.singularux.music.feature.tracklist.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import org.singularux.music.core.permission.MusicPermission
import org.singularux.music.core.permission.MusicPermissionManager
import org.singularux.music.feature.tracklist.domain.GetTrackListByNameUseCase
import org.singularux.music.feature.tracklist.domain.ListenPlaybackMetadataUseCase
import org.singularux.music.feature.tracklist.domain.ListenPlaybackProgressUseCase
import org.singularux.music.feature.tracklist.domain.ListenPlaybackStateUseCase
import org.singularux.music.feature.tracklist.domain.ListenTrackListUseCase
import org.singularux.music.feature.tracklist.ui.TrackListBottomBarData
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class TrackListViewModel @Inject constructor(
    musicPermissionManager: MusicPermissionManager,
    getTrackListByNameUseCase: GetTrackListByNameUseCase,
    listenTrackListUseCase: ListenTrackListUseCase,
    listenPlaybackMetadataUseCase: ListenPlaybackMetadataUseCase,
    listenPlaybackProgressUseCase: ListenPlaybackProgressUseCase,
    listenPlaybackStateUseCase: ListenPlaybackStateUseCase
) : ViewModel() {

    val trackList = listenTrackListUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val searchBarTextFieldState = TextFieldState()
    val searchTrackList = snapshotFlow { searchBarTextFieldState.text }
        .debounce(150)
        .map { getTrackListByNameUseCase(it.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val playbackData = combine(
        listenPlaybackMetadataUseCase(),
        listenPlaybackProgressUseCase(),
        listenPlaybackStateUseCase()
    ) { maybePlaybackMetadata, playbackProgress, playbackState ->
        withContext(Dispatchers.Default) {
            if (playbackState.isEnabled && maybePlaybackMetadata != null) {
                TrackListBottomBarData.Playing(
                    title = maybePlaybackMetadata.title,
                    artistName = maybePlaybackMetadata.artistName,
                    artworkUri = maybePlaybackMetadata.artworkUri,
                    progress = playbackProgress.progress,
                    isPlaying = playbackState.isPlaying
                )
            } else {
                TrackListBottomBarData.Idle
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TrackListBottomBarData.Idle)

    val readMusicPermission = musicPermissionManager.getPermissionString(MusicPermission.READ_MUSIC)
    val readPhoneStatePermission = musicPermissionManager
        .getPermissionString(MusicPermission.READ_PHONE_STATE)

    fun play() {

    }

    fun pause() {

    }

    fun playFromIndex(index: Int) {

    }

    fun shuffle() {

    }

    fun addToQueue() {

    }

}