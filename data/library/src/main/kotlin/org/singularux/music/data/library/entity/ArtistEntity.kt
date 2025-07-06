package org.singularux.music.data.library.entity

data class ArtistEntity(
    val id: Long,
    val name: String,
    val numberOfAlbums: Long,
    val numberOfTracks: Long
)