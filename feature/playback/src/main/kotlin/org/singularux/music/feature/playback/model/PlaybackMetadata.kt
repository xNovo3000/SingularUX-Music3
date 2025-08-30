package org.singularux.music.feature.playback.model

import android.net.Uri
import kotlin.time.Duration

data class PlaybackMetadata(
    val title: String,
    val artistName: String?,
    val artworkUri: Uri?,
    val duration: Duration,
    val playingFrom: String?
)