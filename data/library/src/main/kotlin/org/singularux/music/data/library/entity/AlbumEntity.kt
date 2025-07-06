package org.singularux.music.data.library.entity

data class AlbumEntity(
    val id: Long,
    val title: String,
    val artistId: Long?,
    val artistName: String?,
    val numberOfTracks: Long
)