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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.singularux.music.core.permission.MusicPermission
import org.singularux.music.feature.tracklist.domain.GetPlatformPermissionStringUseCase
import org.singularux.music.feature.tracklist.domain.GetTrackListByNameUseCase
import org.singularux.music.feature.tracklist.domain.ListenPlaybackMetadataUseCase
import org.singularux.music.feature.tracklist.domain.ListenPlaybackProgressUseCase
import org.singularux.music.feature.tracklist.domain.ListenPlaybackStateUseCase
import org.singularux.music.feature.tracklist.domain.ListenTrackListUseCase
import org.singularux.music.feature.tracklist.domain.OverrideTimelineAndSeekToUseCase
import org.singularux.music.feature.tracklist.domain.ActionPauseMusicUseCase
import org.singularux.music.feature.tracklist.domain.ActionPlayMusicUseCase
import org.singularux.music.feature.tracklist.ui.TrackListBottomBarData
import javax.inject.Inject
import kotlin.collections.map

@OptIn(FlowPreview::class)
@HiltViewModel
class TrackListViewModel @Inject constructor(
    getPlatformPermissionStringUseCase: GetPlatformPermissionStringUseCase,
    getTrackListByNameUseCase: GetTrackListByNameUseCase,
    listenTrackListUseCase: ListenTrackListUseCase,
    listenPlaybackMetadataUseCase: ListenPlaybackMetadataUseCase,
    listenPlaybackProgressUseCase: ListenPlaybackProgressUseCase,
    listenPlaybackStateUseCase: ListenPlaybackStateUseCase,
    private val overrideTimelineAndSeekToUseCase: OverrideTimelineAndSeekToUseCase,
    private val actionPlayMusicUseCase: ActionPlayMusicUseCase,
    private val actionPauseMusicUseCase: ActionPauseMusicUseCase
) : ViewModel() {

    val trackList = combine(
        listenTrackListUseCase(),
        listenPlaybackMetadataUseCase()
    ) { trackList, maybePlaybackMetadata ->
        withContext(Dispatchers.Default) {
            trackList.map {
                it.copy(
                    isCurrentlyPlaying = maybePlaybackMetadata?.playingFromExtra == "tracks/${it.id}"
                )
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val searchBarTextFieldState = TextFieldState()
    val searchTrackList = snapshotFlow { searchBarTextFieldState.text }
        .debounce(150)
        .map { getTrackListByNameUseCase(name = it.toString()) }
        .combine(listenPlaybackMetadataUseCase()) { searchTrackList, maybePlaybackMetadata ->
            withContext(Dispatchers.Default) {
                searchTrackList.map {
                    it.copy(
                        isCurrentlyPlaying = maybePlaybackMetadata?.playingFromExtra == "search/${it.id}"
                    )
                }
            }
        }
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

    val readMusicPermission = getPlatformPermissionStringUseCase(MusicPermission.READ_MUSIC)
    val readPhoneStatePermission = getPlatformPermissionStringUseCase(MusicPermission.READ_PHONE_STATE)

    fun play() {
        viewModelScope.launch { actionPlayMusicUseCase() }
    }

    fun pause() {
        viewModelScope.launch { actionPauseMusicUseCase() }
    }

    fun playFromTrackList(index: Int) {
        viewModelScope.launch {
            overrideTimelineAndSeekToUseCase(
                tagPrefix = "tracks",
                newTracks = trackList.value.map { it.copy() },
                index = index
            )
            actionPlayMusicUseCase()
        }
    }

    fun playFromSearchTrackList(index: Int) {
        viewModelScope.launch {
            overrideTimelineAndSeekToUseCase(
                tagPrefix = "search",
                newTracks = searchTrackList.value.map { it.copy() },
                index = index
            )
            actionPlayMusicUseCase()
        }
    }

    fun playShuffled() {
        viewModelScope.launch {
            overrideTimelineAndSeekToUseCase(
                tagPrefix = "tracks",
                newTracks = trackList.value
                    .map { it.copy() }
                    .shuffled(),
                index = 0
            )
            actionPlayMusicUseCase()
        }
    }

    fun addToQueue(index: Int) {
        // TODO: Implement
    }

}