package org.singularux.music.data.playbackstate.timeline.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Duration

@Entity
data class SavedMediaItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    // MediaItem
    val itemId: Long,
    // MediaMetadata
    val title: String,
    val artist: String?,
    val duration: Duration,
    val artworkUri: Uri?,
    // Extras
    val playingFrom: String,
    val addedByUser: Boolean
)