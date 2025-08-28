package org.singularux.music.data.playbackstate.timeline.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.singularux.music.data.playbackstate.timeline.entity.SavedMediaItemEntity

@Dao
interface SavedMediaItemRepository {

    @Query("select * from SavedMediaItemEntity")
    suspend fun getAll(): List<SavedMediaItemEntity>

    @Insert
    suspend fun insertAll(savedMediaItems: List<SavedMediaItemEntity>)

    @Query("delete from SavedMediaItemEntity")
    suspend fun deleteAll()

}