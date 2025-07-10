package org.singularux.music.feature.playback.model

import android.net.Uri

data class PlaybackInfo(
    val title: String,
    val artistsName: String?,
    val artworkUri: Uri?,
)