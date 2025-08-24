package org.singularux.music

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import org.singularux.music.core.ui.MusicSurface
import org.singularux.music.core.ui.MusicTheme
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
                        TrackListRoute(viewModel = hiltViewModel())
                    }
                    entry<MusicRoute.NowPlaying> {
                        // TODO
                    }
                }
            )
        }
    }
}