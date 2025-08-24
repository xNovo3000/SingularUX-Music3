package org.singularux.music.data.library.entity

import android.net.Uri
import kotlin.time.Duration

data class TrackEntity(
    val id: Long,
    val title: String,
    val artistName: String?,
    val duration: Duration,
    val artworkUri: Uri?
)