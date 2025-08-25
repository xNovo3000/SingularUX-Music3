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

        private val URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        private val PROJECTION = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.ALBUM
        )
        private const val SORT_ORDER = MediaStore.Audio.Media.DEFAULT_SORT_ORDER

        private const val GET_ALL_SELECTION = "${MediaStore.Audio.Media.IS_MUSIC} = ? AND " +
                "${MediaStore.Audio.Media.IS_TRASHED} = ?"
        private val GET_ALL_SELECTION_ARGS = arrayOf("1", "0")

        private const val GET_BY_NAME_SELECTION = "${MediaStore.Audio.Media.IS_MUSIC} = ? AND " +
                "${MediaStore.Audio.Media.IS_TRASHED} = ? AND " +
                "(${MediaStore.Audio.Media.TITLE} LIKE ? OR " +
                "${MediaStore.Audio.Media.DISPLAY_NAME} LIKE ?)"
        private val GET_BY_NAME_SELECTION_ARGS = arrayOf("1", "0", "", "")

        private const val ARTWORK_URI_STRING = "content://media/external/audio/albumart"

    }

    override suspend fun getAll(): List<TrackEntity> {
        if (!musicPermissionManager.hasPermission(MusicPermission.READ_MUSIC)) {
            Log.d(TAG, "Missing READ_MUSIC permission")
            return emptyList()
        }
        return withContext(Dispatchers.IO) {
            context.contentResolver.query(
                URI, PROJECTION, GET_ALL_SELECTION, GET_ALL_SELECTION_ARGS, SORT_ORDER
            ).use { cursor ->
                val result = mutableListOf<TrackEntity>()
                while (cursor?.moveToNext() == true) {
                    result.add(
                        element = TrackEntity(
                            id = cursor.getLong(0),
                            title = cursor.getStringOrNull(1) ?: cursor.getString(2),
                            artistName = cursor.getStringOrNull(3)?.let {
                                when (it) {
                                    "<unknown>" -> null
                                    else -> it
                                }
                            },
                            duration = cursor.getLong(4).milliseconds,
                            artworkUri = cursor.getStringOrNull(6)?.let {
                                when (it) {
                                    "<unknown>" -> null
                                    else -> "$ARTWORK_URI_STRING/${cursor.getLongOrNull(5)}".toUri()
                                }
                            }
                        )
                    )
                }
                result
            }
        }
    }

    override suspend fun getAllByName(name: String, limit: Int): List<TrackEntity> {
        if (!musicPermissionManager.hasPermission(MusicPermission.READ_MUSIC)) {
            Log.d(TAG, "Missing READ_MUSIC permission")
            return emptyList()
        }
        val selectionArgs = GET_BY_NAME_SELECTION_ARGS.clone()
        selectionArgs[selectionArgs.size - 2] = "%$name%"
        selectionArgs[selectionArgs.size - 1] = "%$name%"
        return withContext(Dispatchers.IO) {
            context.contentResolver.query(
                URI, PROJECTION, GET_BY_NAME_SELECTION, selectionArgs, SORT_ORDER
            ).use { cursor ->
                val result = mutableListOf<TrackEntity>()
                (0 until limit).forEach { _ ->
                    if (cursor?.moveToNext() == true) {
                        result.add(
                            element = TrackEntity(
                                id = cursor.getLong(0),
                                title = cursor.getStringOrNull(1) ?: cursor.getString(2),
                                artistName = cursor.getStringOrNull(3)?.let {
                                    when (it) {
                                        "<unknown>" -> null
                                        else -> it
                                    }
                                },
                                duration = cursor.getLong(4).milliseconds,
                                artworkUri = cursor.getStringOrNull(6)?.let {
                                    when (it) {
                                        "<unknown>" -> null
                                        else -> "$ARTWORK_URI_STRING/${cursor.getLongOrNull(5)}".toUri()
                                    }
                                }
                            )
                        )
                    }
                }
                result
            }
        }
    }

}