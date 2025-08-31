package org.singularux.music.feature.playback.foreground

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import org.singularux.music.feature.playback.work.RestorePlaybackDataWorker
import org.singularux.music.feature.playback.work.SavePlaybackDataWorker
import javax.inject.Inject

@AndroidEntryPoint
class MusicPlaybackService : MediaSessionService() {

    companion object {
        private const val TAG = "MusicPlaybackService"
        const val INTENT_ORIGIN = "system_ui_notification"
    }

    private var mediaSession: MediaSession? = null
    private var saveWhenPausingListener: Player.Listener? = null
    private var pauseWhenCallingBroadcastReceiver: PauseWhenCallingBroadcastReceiver? = null

    @Inject lateinit var workManager: WorkManager

    override fun onCreate() {
        super.onCreate()
        // Create intent to launch main activity when the user clicks the notification
        val pendingIntent = packageManager.getLaunchIntentForPackage(packageName)!!.let {
            it.putExtra("origin", INTENT_ORIGIN)
            PendingIntent.getActivity(this, 0, it,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }
        // Create current session
        Log.d(TAG, "Creating media session")
        val player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(pendingIntent)
            .build()
        // Listen for changes in the session
        // When the user pauses the playback, the current state is saved
        Log.d(TAG, "Creating and registering playback data saver listener")
        saveWhenPausingListener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (!isPlaying) {
                    workManager.enqueueUniqueWork(
                        uniqueWorkName = SavePlaybackDataWorker.TAG,
                        existingWorkPolicy = ExistingWorkPolicy.REPLACE,
                        request = OneTimeWorkRequestBuilder<SavePlaybackDataWorker>()
                            .addTag(SavePlaybackDataWorker.TAG)
                            .build()
                    )
                }
            }
        }
        mediaSession!!.player.addListener(saveWhenPausingListener!!)
        // Listen for calls
        Log.d(TAG, "Registering call state receiver")
        val phoneCallIntentFilter = IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        pauseWhenCallingBroadcastReceiver = PauseWhenCallingBroadcastReceiver(mediaSession = mediaSession!!)
        ContextCompat.registerReceiver(this, pauseWhenCallingBroadcastReceiver,
            phoneCallIntentFilter, ContextCompat.RECEIVER_EXPORTED)
        // Restore playback data
        Log.d(TAG, "Restoring playback data")
        workManager.enqueueUniqueWork(
            uniqueWorkName = RestorePlaybackDataWorker.TAG,
            existingWorkPolicy = ExistingWorkPolicy.REPLACE,
            request = OneTimeWorkRequestBuilder<RestorePlaybackDataWorker>()
                .addTag(RestorePlaybackDataWorker.TAG)
                .build()
        )
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d(TAG, "User dismissed the activity, pausing the player")
        // Stop playback if user dismisses the app
        mediaSession?.player?.playWhenReady = false
        stopSelf()
    }

    override fun onDestroy() {
        // Stop listening for calls
        Log.d(TAG, "Unregistering call state receiver")
        pauseWhenCallingBroadcastReceiver = pauseWhenCallingBroadcastReceiver?.also {
            unregisterReceiver(it)
            null
        }
        // Stop listening for changes in the session
        Log.d(TAG, "Unregistering playback data saver listener")
        saveWhenPausingListener = saveWhenPausingListener?.also {
            mediaSession?.player?.removeListener(it)
            null
        }
        // Destroy session
        Log.d(TAG, "Destroying session")
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