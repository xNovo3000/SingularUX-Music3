package org.singularux.music.feature.playback.foreground

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
    private var phoneStateBroadcastReceiver: PhoneStateBroadcastReceiver? = null

    override fun onCreate() {
        super.onCreate()
        // Create session
        val player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()
        // React to phone state change
        phoneStateBroadcastReceiver = PhoneStateBroadcastReceiver(mediaSession!!)
        ContextCompat.registerReceiver(this, phoneStateBroadcastReceiver,
            IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED),
            ContextCompat.RECEIVER_NOT_EXPORTED)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        mediaSession?.player?.playWhenReady = false
        stopSelf()
    }

    override fun onDestroy() {
        // Stop reacting to phone state change
        unregisterReceiver(phoneStateBroadcastReceiver)
        // Release session
        mediaSession?.run {
            player.release()
            release()
        }
        mediaSession = null
        super.onDestroy()
    }

    override fun onGetSession(
        controllerInfo: MediaSession.ControllerInfo
    ): MediaSession? = mediaSession

}