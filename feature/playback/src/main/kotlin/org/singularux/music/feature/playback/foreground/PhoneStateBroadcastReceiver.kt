package org.singularux.music.feature.playback.foreground

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import androidx.media3.session.MediaSession

class PhoneStateBroadcastReceiver(private val mediaSession: MediaSession) : BroadcastReceiver() {

    private var wasPlayingBeforeCallStarted = false

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            when (state) {
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    // Resume if before the call the session was playing
                    mediaSession.player.playWhenReady = wasPlayingBeforeCallStarted
                }
                TelephonyManager.EXTRA_STATE_RINGING, TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    // Pause if required and save the state for later
                    wasPlayingBeforeCallStarted = mediaSession.player.isPlaying
                    if (wasPlayingBeforeCallStarted) {
                        mediaSession.player.pause()
                    }
                }
            }
        }
    }

}