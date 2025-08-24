package org.singularux.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import org.singularux.music.core.playback.MusicControllerFacade
import javax.inject.Inject

@AndroidEntryPoint
class MusicActivity : ComponentActivity() {

    @Inject lateinit var musicControllerFacade: MusicControllerFacade

    override fun onCreate(savedInstanceState: Bundle?) {
        // Start activity with splash screen and edge-to-edge
        installSplashScreen()
            .setKeepOnScreenCondition { !musicControllerFacade.isReady }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // Bootstrap into Compose UI
        setContent { MusicUI() }
    }

    override fun onDestroy() {
        // Release the MediaController only when the activity is being killed
        if (isFinishing) musicControllerFacade.release()
        super.onDestroy()
    }

}