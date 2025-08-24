package org.singularux.music.data.library.repository

import android.content.Context
import android.util.Log
import org.singularux.music.core.permission.MusicPermission
import org.singularux.music.core.permission.MusicPermissionManager
import org.singularux.music.data.library.entity.TrackEntity

class TrackRepositoryAndroid(
    private val context: Context,
    private val musicPermissionManager: MusicPermissionManager
) : TrackRepository {

    companion object {
        private const val TAG = "TrackRepositoryAndroid"
    }

    override suspend fun getAll(): List<TrackEntity> {
        if (!musicPermissionManager.hasPermission(MusicPermission.READ_MUSIC)) {
            Log.d(TAG, "Missing READ_MUSIC permission")
            return emptyList()
        }
        TODO("Not yet implemented")
    }

    override suspend fun getAllByName(name: String, limit: Int): List<TrackEntity> {
        TODO("Not yet implemented")
    }

}