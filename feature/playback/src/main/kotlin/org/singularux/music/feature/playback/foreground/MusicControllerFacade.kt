package org.singularux.music.feature.playback.foreground

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicControllerFacade(
    context: Context,
    private val coroutineScope: CoroutineScope
) {

    companion object {
        private const val TAG = "MusicControllerFacade"
    }

    private val mediaControllerFuture: ListenableFuture<MediaController>
    var mediaController: MediaController? = null
        private set

    val isReady: Boolean
        get() = mediaController?.isConnected == true

    init {
        // Load async
        val componentName = ComponentName(context, MusicPlaybackService::class.java)
        val sessionToken = SessionToken(context, componentName)
        mediaControllerFuture = MediaController.Builder(context, sessionToken)
            .buildAsync()
        // Retrieve instance
        coroutineScope.launch {
            try {
                mediaController = mediaControllerFuture.await()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to retrieve the MediaController", e)
            }
        }
    }

    fun addListener(listener: Player.Listener) {
        coroutineScope.launch {
            val mediaController = mediaControllerFuture.await()
            withContext(Dispatchers.Main) {
                mediaController.addListener(listener)
            }
        }
    }

    fun removeListener(listener: Player.Listener) {
        coroutineScope.launch {
            val mediaController = mediaControllerFuture.await()
            withContext(Dispatchers.Main) {
                mediaController.removeListener(listener)
            }
        }
    }

}