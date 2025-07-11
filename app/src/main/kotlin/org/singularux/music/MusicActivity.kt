package org.singularux.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import org.singularux.music.feature.playback.foreground.MusicControllerFacade
import javax.inject.Inject

@AndroidEntryPoint
class MusicActivity : ComponentActivity() {

    @Inject lateinit var musicControllerFacade: MusicControllerFacade

    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
            .setKeepOnScreenCondition { !musicControllerFacade.isReady }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent { MusicUI() }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release MediaController
        musicControllerFacade.release()
    }

}