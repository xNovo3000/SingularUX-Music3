package org.singularux.music.core.playback

import android.content.Intent
import android.content.IntentFilter
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicPlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private var pauseWhenCallingBroadcastReceiver: PauseWhenCallingBroadcastReceiver? = null

    override fun onCreate() {
        super.onCreate()
        // Create current session
        val player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()
        // Listen for calls
        val phoneCallIntentFilter = IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        pauseWhenCallingBroadcastReceiver = PauseWhenCallingBroadcastReceiver(mediaSession = mediaSession!!)
        ContextCompat.registerReceiver(this, pauseWhenCallingBroadcastReceiver,
            phoneCallIntentFilter, ContextCompat.RECEIVER_EXPORTED)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        // Stop playback if user dismisses the app
        mediaSession?.player?.playWhenReady = false
        stopSelf()
    }

    override fun onDestroy() {
        // Stop listening for calls
        pauseWhenCallingBroadcastReceiver = pauseWhenCallingBroadcastReceiver?.also {
            unregisterReceiver(it)
            null
        }
        // Destroy session
        mediaSession = mediaSession?.also {
            it.player.release()
            it.release()
            null
        }
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

}