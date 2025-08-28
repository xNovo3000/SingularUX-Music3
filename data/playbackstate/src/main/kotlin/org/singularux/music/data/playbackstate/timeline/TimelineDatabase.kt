package org.singularux.music.data.playbackstate.timeline

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.singularux.music.data.playbackstate.timeline.converter.DurationConverter
import org.singularux.music.data.playbackstate.timeline.converter.UriConverter
import org.singularux.music.data.playbackstate.timeline.entity.SavedMediaItemEntity
import org.singularux.music.data.playbackstate.timeline.repository.SavedMediaItemRepository

@Database(entities = [SavedMediaItemEntity::class], version = 1)
@TypeConverters(value = [DurationConverter::class, UriConverter::class])
abstract class TimelineDatabase : RoomDatabase() {
    abstract fun getSavedMediaItemRepository(): SavedMediaItemRepository
}