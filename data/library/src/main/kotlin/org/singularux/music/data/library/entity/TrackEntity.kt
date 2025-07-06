package org.singularux.music.data.library.entity

import android.net.Uri
import kotlin.time.Duration

data class TrackEntity(
    val id: Long,
    val title: String,
    val artistId: Long?,
    val artistName: String?,
    val albumId: Long?,
    val artwork: Uri?,
    val duration: Duration
)