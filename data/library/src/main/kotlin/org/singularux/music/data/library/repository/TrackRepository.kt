package org.singularux.music.data.library.repository

import org.singularux.music.data.library.entity.TrackEntity

interface TrackRepository {
    suspend fun getAll(): List<TrackEntity>
    suspend fun getAllByName(name: String, limit: Int): List<TrackEntity>
}