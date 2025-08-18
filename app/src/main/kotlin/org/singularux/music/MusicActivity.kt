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
        installSplashScreen()
            .setKeepOnScreenCondition { !musicControllerFacade.isReady }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent { MusicUI() }
    }

    override fun onDestroy() {
        musicControllerFacade.release()
        super.onDestroy()
    }

}