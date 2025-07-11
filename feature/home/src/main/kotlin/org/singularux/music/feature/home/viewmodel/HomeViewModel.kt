package org.singularux.music.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.singularux.music.core.permission.MusicPermission
import org.singularux.music.core.permission.MusicPermissionManager
import org.singularux.music.feature.home.presentation.ListenTrackListUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    listenTrackListUseCase: ListenTrackListUseCase,
    musicPermissionManager: MusicPermissionManager
) : ViewModel() {

    val trackItemDataList = listenTrackListUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val readMusicPermission = musicPermissionManager.getPermissionString(MusicPermission.READ_MUSIC)

}