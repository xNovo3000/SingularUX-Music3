package org.singularux.music.core.playback

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.asDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicControllerFacade(
    context: Context,
    private val coroutineScope: CoroutineScope
) {

    companion object {
        private const val TAG = "MusicControllerFacade"
    }

    private val mediaControllerDeferred: Deferred<MediaController>
    var mediaController: MediaController? = null
        private set

    init {
        // Create the MediaController
        val componentName = ComponentName(context, MusicPlaybackService::class.java)
        val sessionToken = SessionToken(context, componentName)
        mediaControllerDeferred = MediaController.Builder(context, sessionToken)
            .buildAsync()
            .asDeferred()
        // Force get the instance
        coroutineScope.launch {
            try {
                mediaController = mediaControllerDeferred.await()
            } catch (e: Exception) {
                Log.e(TAG, "Cannot get MediaController", e)
            }
        }
    }

    fun addListener(listener: Player.Listener) {
        coroutineScope.launch {
            val mediaController = try {
                mediaControllerDeferred.await()
            } catch (e: Exception) {
                Log.e(TAG, "Cannot get MediaController", e)
                return@launch
            }
            withContext(Dispatchers.Main) {
                mediaController.addListener(listener)
            }
        }
    }

    fun removeListener(listener: Player.Listener) {
        coroutineScope.launch {
            val mediaController = try {
                mediaControllerDeferred.await()
            } catch (e: Exception) {
                Log.e(TAG, "Cannot get MediaController", e)
                return@launch
            }
            withContext(Dispatchers.Main) {
                mediaController.removeListener(listener)
            }
        }
    }

    fun release() {
        mediaController?.release() ?: coroutineScope.launch {
            val mediaController = try {
                mediaControllerDeferred.await()
            } catch (e: Exception) {
                Log.e(TAG, "Cannot get MediaController", e)
                return@launch
            }
            withContext(Dispatchers.Main) {
                mediaController.release()
            }
        }
    }

    val isReady: Boolean
        get() = mediaController != null

}