package org.singularux.music.data.playbackstate.timeline.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedMediaItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
)