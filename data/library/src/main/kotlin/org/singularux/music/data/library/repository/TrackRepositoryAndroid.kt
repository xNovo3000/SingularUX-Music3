package org.singularux.music.data.library.repository

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.singularux.music.core.permission.MusicPermission
import org.singularux.music.core.permission.MusicPermissionManager
import org.singularux.music.data.library.entity.TrackEntity
import kotlin.time.Duration.Companion.milliseconds

internal class TrackRepositoryAndroid(
    private val context: Context,
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
        private const val GET_ALL_SELECTION = "${MediaStore.Audio.Media.IS_TRASHED} = ? " +
                "AND ${MediaStore.Audio.Media.IS_MUSIC} = ?"
        private val GET_ALL_SELECTION_ARGS = listOf("0", "1")
        private const val GET_ALL_SORT_ORDER = MediaStore.Audio.Media.DEFAULT_SORT_ORDER

        private const val GET_ALL_BY_TITLE_LIKE_SELECTION = "${MediaStore.Audio.Media.IS_TRASHED} = ? " +
                "AND ${MediaStore.Audio.Media.IS_MUSIC} = ? AND " +
                "(${MediaStore.Audio.Media.TITLE} LIKE ? OR ${MediaStore.Audio.Media.DISPLAY_NAME} LIKE ?)"

        private const val ARTWORK_URI_STRING = "content://media/external/audio/albumart"

    }

    override suspend fun getAll(): List<TrackEntity> {
        // Check for READ_MUSIC permission
        // If not present, return empty list
        // If present, query MediaStore
        if (!musicPermissionManager.hasPermission(MusicPermission.READ_MUSIC)) {
            Log.d(TAG, "getAll(): missing permission READ_MUSIC")
            return emptyList()
        }
        return withContext(Dispatchers.IO) {
            context.contentResolver.query(
                GET_ALL_URI,
                GET_ALL_PROJECTION,
                GET_ALL_SELECTION,
                GET_ALL_SELECTION_ARGS.toTypedArray(),
                GET_ALL_SORT_ORDER
            ).use { cursor ->
                withContext(Dispatchers.Default) {
                    val result = mutableListOf<TrackEntity>()
                    while (cursor?.moveToNext() == true) {
                        val albumId = cursor.getLongOrNull(5)
                        result.add(
                            element = TrackEntity(
                                id = cursor.getLong(0),
                                title = cursor.getStringOrNull(1) ?: cursor.getString(2),
                                artistId = cursor.getLongOrNull(3),
                                artistName = cursor.getStringOrNull(4),
                                albumId = albumId,
                                artworkUri = if (albumId != null) "${ARTWORK_URI_STRING}/${albumId}".toUri() else null,
                                duration = cursor.getLong(6).milliseconds
                            )
                        )
                    }
                    result
                }
            }
        }
    }

    override suspend fun getAllByTitleLike(title: String): List<TrackEntity> {
        // Check for READ_MUSIC permission
        // If not present, return empty list
        // If present, query MediaStore
        // If title is empty, return empty list
        if (title.isEmpty()) {
            return emptyList()
        }
        if (!musicPermissionManager.hasPermission(MusicPermission.READ_MUSIC)) {
            Log.d(TAG, "getAll(): missing permission READ_MUSIC")
            return emptyList()
        }
        return withContext(Dispatchers.IO) {
            context.contentResolver.query(
                GET_ALL_URI,
                GET_ALL_PROJECTION,
                GET_ALL_BY_TITLE_LIKE_SELECTION,
                GET_ALL_SELECTION_ARGS.toMutableList()
                    .apply {
                        add("%$title%")
                        add("%$title%")
                    }
                    .toTypedArray(),
                GET_ALL_SORT_ORDER
            ).use { cursor ->
                withContext(Dispatchers.Default) {
                    val result = mutableListOf<TrackEntity>()
                    while (cursor?.moveToNext() == true) {
                        val albumId = cursor.getLongOrNull(5)
                        result.add(
                            element = TrackEntity(
                                id = cursor.getLong(0),
                                title = cursor.getStringOrNull(1) ?: cursor.getString(2),
                                artistId = cursor.getLongOrNull(3),
                                artistName = cursor.getStringOrNull(4),
                                albumId = albumId,
                                artworkUri = if (albumId != null) "${ARTWORK_URI_STRING}/${albumId}".toUri() else null,
                                duration = cursor.getLong(6).milliseconds
                            )
                        )
                    }
                    result
                }
            }
        }
    }

}