package org.singularux.music.feature.playback.model

data class PlaybackState(
    val isEnabled: Boolean,
    val isPlaying: Boolean,
    val hasNext: Boolean
)