package org.singularux.music.core.playback

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import androidx.media3.session.MediaSession

class PauseWhenCallingBroadcastReceiver(private val mediaSession: MediaSession) : BroadcastReceiver() {

    private var wasPlayingWhenInterrupted = false

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            when (intent.getStringExtra(TelephonyManager.EXTRA_STATE)) {
                TelephonyManager.EXTRA_STATE_RINGING, TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    // Save state and pause the player if required
                    wasPlayingWhenInterrupted = mediaSession.player.isPlaying
                    if (wasPlayingWhenInterrupted) {
                        mediaSession.player.pause()
                    }
                }
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    // Load saved state
                    mediaSession.player.playWhenReady = wasPlayingWhenInterrupted
                }
            }
        }
    }

}