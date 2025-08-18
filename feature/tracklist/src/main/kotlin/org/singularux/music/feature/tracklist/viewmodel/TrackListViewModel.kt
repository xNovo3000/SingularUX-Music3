package org.singularux.music.feature.tracklist.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.singularux.music.core.permission.MusicPermission
import org.singularux.music.core.permission.MusicPermissionManager
import org.singularux.music.feature.tracklist.domain.GetTrackListByNameUseCase
import org.singularux.music.feature.tracklist.domain.ListenTrackListUseCase
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class TrackListViewModel @Inject constructor(
    getTrackListByNameUseCase: GetTrackListByNameUseCase,
    listenTrackListUseCase: ListenTrackListUseCase
) : ViewModel() {

    val trackList = listenTrackListUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val searchBarTextFieldState = TextFieldState()
    val searchTrackList = snapshotFlow { searchBarTextFieldState.text }
        .debounce(150)
        .map { getTrackListByNameUseCase(it.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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