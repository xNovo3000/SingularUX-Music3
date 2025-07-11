package org.singularux.music.feature.home.viewmodel

import android.util.Log
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
import org.singularux.music.feature.home.presentation.GetTrackListByNameUseCase
import org.singularux.music.feature.home.presentation.ListenTrackListUseCase
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    listenTrackListUseCase: ListenTrackListUseCase,
    getTrackListByNameUseCase: GetTrackListByNameUseCase,
    musicPermissionManager: MusicPermissionManager
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    val trackItemDataList = listenTrackListUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val readMusicPermission = musicPermissionManager.getPermissionString(MusicPermission.READ_MUSIC)

    val searchTextFieldState = TextFieldState()
    @FlowPreview val trackItemDataSearchList = snapshotFlow { searchTextFieldState.text }
        .debounce(300.milliseconds)
        .map { title ->
            Log.d(TAG, "Search: received $title")
            getTrackListByNameUseCase(title.toString())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

}