package org.singularux.music

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.core.util.Consumer
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import kotlinx.serialization.Serializable
import org.singularux.music.core.ui.MusicPopTransitionSpec
import org.singularux.music.core.ui.MusicPredictivePopTransitionSpec
import org.singularux.music.core.ui.MusicSurface
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.core.ui.MusicTransitionSpec
import org.singularux.music.feature.nowplaying.ui.NowPlayingRoute
import org.singularux.music.feature.playback.foreground.MusicPlaybackService
import org.singularux.music.feature.tracklist.ui.TrackListRoute

sealed class MusicRoute : NavKey {
    @Serializable data object TrackList : MusicRoute()
    @Serializable data object NowPlaying : MusicRoute()
}

@Composable
fun MusicUI() {
    MusicTheme {
        MusicSurface {
            val backStack = rememberNavBackStack(MusicRoute.TrackList)
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryDecorators = listOf(
                    rememberSceneSetupNavEntryDecorator(),
                    rememberSavedStateNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                entryProvider = entryProvider {
                    entry<MusicRoute.TrackList> {
                        TrackListRoute(
                            viewModel = hiltViewModel(),
                            onGoToNowPlayingRoute = { backStack.add(MusicRoute.NowPlaying) }
                        )
                    }
                    entry<MusicRoute.NowPlaying> {
                        NowPlayingRoute(
                            viewModel = hiltViewModel(),
                            onGoBack = { backStack.removeLastOrNull() }
                        )
                    }
                },
                transitionSpec = MusicTransitionSpec,
                popTransitionSpec = MusicPopTransitionSpec,
                predictivePopTransitionSpec = MusicPredictivePopTransitionSpec
            )
            // Go to NowPlaying when user clicks on the notification
            val activity = LocalActivity.current as MusicActivity
            DisposableEffect(Unit) {
                val intentListener = object : Consumer<Intent> {
                    override fun accept(value: Intent) {
                        if (value.getStringExtra("origin") == MusicPlaybackService.INTENT_ORIGIN) {
                            if (backStack.lastOrNull() != MusicRoute.NowPlaying) {
                                backStack.add(MusicRoute.NowPlaying)
                            }
                        }
                    }
                }
                activity.addOnNewIntentListener(listener = intentListener)
                onDispose { activity.removeOnNewIntentListener(listener = intentListener) }
            }
        }
    }
}