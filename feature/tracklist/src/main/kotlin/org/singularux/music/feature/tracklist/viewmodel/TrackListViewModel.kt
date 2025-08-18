package org.singularux.music.feature.tracklist.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.singularux.music.core.permission.MusicPermission
import org.singularux.music.core.permission.MusicPermissionManager
import javax.inject.Inject

@HiltViewModel
class TrackListViewModel @Inject constructor(
    musicPermissionManager: MusicPermissionManager
) : ViewModel() {

    val searchBarTextFieldState = TextFieldState()

    val readMusicPermission = musicPermissionManager.getPermissionString(MusicPermission.READ_MUSIC)

    fun play() {

    }

    fun pause() {

    }

    fun playFromIndex(index: Int) {

    }

    fun shuffle() {

    }

}