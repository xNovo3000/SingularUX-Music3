package org.singularux.music.data.library.repository

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.singularux.music.core.permission.MusicPermission
import org.singularux.music.core.permission.MusicPermissionManager
import org.singularux.music.data.library.entity.TrackEntity
import javax.inject.Inject

@ActivityRetainedScoped
class TrackRepositoryAndroid @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val musicPermissionManager: MusicPermissionManager
) : TrackRepository {

    companion object {
        private const val TAG = "TrackRepositoryAndroid"
        private val GET_ALL_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        private val GET_ALL_PROJECTION = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ARTIST_ID, MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION
        )
    }

    override suspend fun getAll(): List<TrackEntity> {
        // Check for READ_MUSIC permission
        // If not present, return empty list
        // If present, query MediaStore
        if (!musicPermissionManager.hasPermission(MusicPermission.READ_MUSIC)) {
            Log.i(TAG, "getAll(): missing permission READ_MUSIC")
            return emptyList()
        }
        // context.contentResolver.query()
    }

}