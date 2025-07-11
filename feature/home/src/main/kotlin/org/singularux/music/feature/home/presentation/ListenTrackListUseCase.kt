package org.singularux.music.feature.home.presentation

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.singularux.music.core.permission.MusicPermission
import org.singularux.music.core.permission.MusicPermissionManager
import org.singularux.music.data.library.repository.TrackRepository
import org.singularux.music.feature.home.ui.TrackItemData
import javax.inject.Inject

class ListenTrackListUseCase @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val trackRepository: TrackRepository,
    private val musicPermissionManager: MusicPermissionManager
) {
    
    companion object {
        private const val TAG = "ListenTrackListUseCase"
        private val URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }

    operator fun invoke(): Flow<List<TrackItemData>> = callbackFlow {
        // Register an observer if permission is present and force first update
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            var currentJob: Job? = null
            override fun onChange(selfChange: Boolean) {
                currentJob?.cancel()
                currentJob = launch {
                    val trackEntityList = trackRepository.getAll()
                    val trackItemDataList = withContext(Dispatchers.Default) {
                        trackEntityList.map { trackEntity ->
                            TrackItemData(
                                id = trackEntity.id,
                                title = trackEntity.title,
                                artistsName = trackEntity.artistName,
                                duration = trackEntity.duration,
                                artworkUri = trackEntity.artworkUri,
                                isCurrentlyPlaying = false
                            )
                        }
                    }
                    send(trackItemDataList)
                }
            }
        }
        if (musicPermissionManager.hasPermission(MusicPermission.READ_MUSIC)) {
            context.contentResolver.registerContentObserver(URI, false, observer)
            observer.onChange(false)
            Log.d(TAG, "Registered observer $observer")
        } else {
            Log.d(TAG, "Missing READ_MUSIC permission, cannot register observer $observer")
        }
        awaitClose {
            context.contentResolver.unregisterContentObserver(observer)
            Log.d(TAG, "Unregistering observer $observer")
        }
    }

}