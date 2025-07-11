package org.singularux.music

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.singularux.music.core.ui.MusicSurface
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.feature.home.ui.HomeRoute

@Serializable
sealed class MusicRoute {
    @Serializable data object Home : MusicRoute()
    @Serializable data object NowPlaying : MusicRoute()
}

@Composable
fun MusicUI() {
    MusicTheme {
        MusicSurface {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = MusicRoute.Home
            ) {
                composable<MusicRoute.Home> {
                    HomeRoute(
                        homeViewModel = hiltViewModel(),
                        playbackBarViewModel = hiltViewModel(),
                        onGoToPlaybackRoute = { navController.navigate(MusicRoute.NowPlaying) }
                    )
                }
            }
        }
    }
}