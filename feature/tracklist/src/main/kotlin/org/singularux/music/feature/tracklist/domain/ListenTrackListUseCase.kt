package org.singularux.music.feature.tracklist.domain

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
import org.singularux.music.feature.tracklist.ui.TrackItemData
import javax.inject.Inject

class ListenTrackListUseCase @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val musicPermissionManager: MusicPermissionManager,
    private val trackRepository: TrackRepository
) {

    companion object {
        private const val TAG = "ListenTrackListUseCase"
    }

    operator fun invoke(): Flow<List<TrackItemData>> = callbackFlow {
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            private var job: Job? = null
            override fun onChange(selfChange: Boolean) {
                Log.d(TAG, "Received onChange request")
                job?.cancel()
                job = launch {
                    val result = withContext(Dispatchers.Default) {
                        trackRepository.getAll()
                            .map { trackEntity ->
                                TrackItemData(
                                    id = trackEntity.id,
                                    title = trackEntity.title,
                                    artistName = trackEntity.artistName,
                                    duration = trackEntity.duration,
                                    artworkUri = trackEntity.artworkUri,
                                    isCurrentlyPlaying = false  // Will be populated by the VM
                                )
                            }
                    }
                    send(element = result)
                }
            }
        }
        if (musicPermissionManager.hasPermission(MusicPermission.READ_MUSIC)) {
            context.contentResolver.registerContentObserver(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true, observer
            )
        }
        observer.onChange(false)
        awaitClose { context.contentResolver.unregisterContentObserver(observer) }
    }

}