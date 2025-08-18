package org.singularux.music.feature.tracklist.model

import android.net.Uri

data class PlaybackMetadata(
    val title: String,
    val artistName: String?,
    val artworkUri: Uri?,
    val isPlaying: Boolean
)