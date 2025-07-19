package org.singularux.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import org.singularux.music.core.permission.MusicPermission
import org.singularux.music.core.permission.MusicPermissionManager
import org.singularux.music.feature.playback.foreground.MusicControllerFacade
import javax.inject.Inject

@AndroidEntryPoint
class MusicActivity : ComponentActivity() {

    @Inject lateinit var musicPermissionManager: MusicPermissionManager
    @Inject lateinit var musicControllerFacade: MusicControllerFacade

    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
            .setKeepOnScreenCondition { !musicControllerFacade.isReady }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent { MusicUI() }
        // Request all permissions at once
        val permissions = listOf(MusicPermission.READ_MUSIC, MusicPermission.READ_CALL_STATE)
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}
            .launch(musicPermissionManager.getPermissionStrings(permissions).toTypedArray())
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release MediaController
        musicControllerFacade.release()
    }

}