package org.singularux.music.feature.playback.foreground

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.guava.asDeferred
import kotlinx.coroutines.launch

class MusicControllerFacade(context: Context, coroutineScope: CoroutineScope) {

    companion object {
        private const val TAG = "MusicControllerFacade"
    }

    private val mediaControllerDeferred: Deferred<MediaController>
    var mediaController: MediaController? = null
        private set

    val isReady: Boolean
        get() = mediaController?.isConnected == true

    init {
        // Load async
        val componentName = ComponentName(context, MusicPlaybackService::class.java)
        val sessionToken = SessionToken(context, componentName)
        mediaControllerDeferred = MediaController.Builder(context, sessionToken)
            .buildAsync()
            .asDeferred()
        // Retrieve instance
        coroutineScope.launch {
            try {
                mediaController = mediaControllerDeferred.await()
                Log.d(TAG, "Successfully started MediaController")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to retrieve the MediaController", e)
            }
        }
    }

}