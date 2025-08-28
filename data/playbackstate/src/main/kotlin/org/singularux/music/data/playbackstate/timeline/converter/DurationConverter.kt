package org.singularux.music.data.playbackstate.timeline.converter

import androidx.room.TypeConverter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class DurationConverter {

    @TypeConverter
    fun fromDurationToLong(value: Duration?): Long? {
        return value?.inWholeMilliseconds
    }

    @TypeConverter
    fun fromLongToDuration(value: Long?): Duration? {
        return value?.milliseconds
    }

}