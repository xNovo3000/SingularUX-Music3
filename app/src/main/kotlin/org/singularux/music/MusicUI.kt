package org.singularux.music

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import kotlinx.serialization.Serializable
import org.singularux.music.core.ui.MusicSurface
import org.singularux.music.core.ui.MusicTheme

@Serializable
data object Home : NavKey

@Composable
fun MusicUI() {
    MusicTheme {
        MusicSurface {
            val backStack = rememberNavBackStack(Home)
            NavDisplay(
                backStack = backStack,
                entryDecorators = listOf(
                    rememberSavedStateNavEntryDecorator(),
                    rememberSceneSetupNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                entryProvider = entryProvider {

                }
            )
        }
    }
}