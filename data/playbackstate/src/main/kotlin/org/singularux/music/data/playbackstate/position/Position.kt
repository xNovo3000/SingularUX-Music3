package org.singularux.music.data.playbackstate.position

import kotlinx.serialization.Serializable

@Serializable
data class Position(
    val index: Int,
    val positionMs: Long
)