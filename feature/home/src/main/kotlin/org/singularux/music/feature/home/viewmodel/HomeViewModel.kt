package org.singularux.music.feature.home.viewmodel

import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
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
import org.singularux.music.core.permission.MusicPermissionManager
import org.singularux.music.feature.home.presentation.GetTrackListByNameUseCase
import org.singularux.music.feature.home.presentation.ListenTrackListUseCase
import org.singularux.music.feature.playback.foreground.MusicControllerFacade
import org.singularux.music.feature.playback.presentation.ListenPlaybackInfoUseCase
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    listenTrackListUseCase: ListenTrackListUseCase,
    listenPlaybackInfoUseCase: ListenPlaybackInfoUseCase,
    getTrackListByNameUseCase: GetTrackListByNameUseCase,
    musicPermissionManager: MusicPermissionManager,
    private val musicControllerFacade: MusicControllerFacade
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    val trackItemDataList = combine(
        listenTrackListUseCase(),
        listenPlaybackInfoUseCase()
    ) { trackItemDataTempList, maybePlaybackInfo ->
        trackItemDataTempList.map { trackItemData ->
            trackItemData.copy(
                isCurrentlyPlaying = maybePlaybackInfo?.mediaId == "tracks/${trackItemData.id}"
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val readMusicPermission = musicPermissionManager.getPermissionString(MusicPermission.READ_MUSIC)

    val searchTextFieldState = TextFieldState()
    @FlowPreview val trackItemDataSearchList = snapshotFlow { searchTextFieldState.text }
        .debounce(300.milliseconds)
        .map { title ->
            Log.d(TAG, "Search: received $title")
            getTrackListByNameUseCase(title.toString())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun playFromTrackList(index: Int) {
        // Get a snapshot of the current list and convert it to MediaItems
        val mediaItems = trackItemDataList.value.map { trackItemData ->
            val mediaMetadata = MediaMetadata.Builder()
                .setTitle(trackItemData.title)
                .setArtist(trackItemData.artistsName)
                .setDurationMs(trackItemData.duration.inWholeMilliseconds)
                .setArtworkUri(trackItemData.artworkUri)
                .build()
            val uri = "${MediaStore.Audio.Media.EXTERNAL_CONTENT_URI}/${trackItemData.id}".toUri()
            MediaItem.Builder()
                .setMediaId("tracks/${trackItemData.id}")
                .setUri(uri)
                .setMediaMetadata(mediaMetadata)
                .build()
        }
        // Feed it to the controller overriding the current timeline
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                musicControllerFacade.mediaController?.run {
                    clearMediaItems()
                    addMediaItems(mediaItems)
                    seekTo(index, 0)
                    play()
                }
            }
        }
    }

    @FlowPreview
    fun playFromSearchTrackList(index: Int) {
        // Get a snapshot of the current list and convert it to MediaItems
        val mediaItems = trackItemDataSearchList.value.map { trackItemData ->
            val mediaMetadata = MediaMetadata.Builder()
                .setTitle(trackItemData.title)
                .setArtist(trackItemData.artistsName)
                .setDurationMs(trackItemData.duration.inWholeMilliseconds)
                .setArtworkUri(trackItemData.artworkUri)
                .build()
            val uri = "${MediaStore.Audio.Media.EXTERNAL_CONTENT_URI}/${trackItemData.id}".toUri()
            MediaItem.Builder()
                .setMediaId("search/${trackItemData.id}")
                .setUri(uri)
                .setMediaMetadata(mediaMetadata)
                .build()
        }
        // Feed it to the controller overriding the current timeline
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                musicControllerFacade.mediaController?.run {
                    clearMediaItems()
                    addMediaItems(mediaItems)
                    seekTo(index, 0)
                    play()
                }
            }
        }
    }

}