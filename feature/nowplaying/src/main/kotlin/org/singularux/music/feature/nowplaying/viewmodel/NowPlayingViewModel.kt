package org.singularux.music.feature.nowplaying.viewmodel

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderState
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalMaterial3Api::class)
class NowPlayingViewModel @Inject constructor() : ViewModel() {

    val sliderState = SliderState()

}