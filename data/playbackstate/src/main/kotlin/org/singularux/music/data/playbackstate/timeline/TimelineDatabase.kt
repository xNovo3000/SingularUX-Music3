package org.singularux.music.data.playbackstate.timeline

import androidx.room.Database
import androidx.room.RoomDatabase
import org.singularux.music.data.playbackstate.timeline.entity.SavedMediaItemEntity
import org.singularux.music.data.playbackstate.timeline.repository.SavedMediaItemRepository

@Database(entities = [SavedMediaItemEntity::class], version = 1)
abstract class TimelineDatabase : RoomDatabase() {
    abstract fun getSavedMediaItemRepository(): SavedMediaItemRepository
}