package org.singularux.music.feature.nowplaying.model

data class PlaybackState(
    val isEnabled: Boolean,
    val isPlaying: Boolean,
    val hasNext: Boolean
)