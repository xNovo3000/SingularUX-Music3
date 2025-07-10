package org.singularux.music.feature.playback.model

data class PlaybackState(
    val isReady: Boolean,
    val isPlaying: Boolean,
    val hasNextItem: Boolean,
)