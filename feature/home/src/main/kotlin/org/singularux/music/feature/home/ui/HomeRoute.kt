package org.singularux.music.feature.home.ui

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.singularux.music.feature.home.viewmodel.HomeViewModel
import org.singularux.music.feature.playback.ui.PlaybackBar
import org.singularux.music.feature.playback.ui.PlaybackBarAction
import org.singularux.music.feature.playback.viewmodel.PlaybackBarViewModel

@ExperimentalMaterial3ExpressiveApi
@ExperimentalMaterial3Api
@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel,
    playbackBarViewModel: PlaybackBarViewModel,
    onGoToPlaybackRoute: () -> Unit,
) {
    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            val textFieldState = rememberTextFieldState()
            LaunchedEffect(textFieldState.text) {
                // TODO: Tell VM that search text changed
            }
            val searchBarState = rememberSearchBarState()
            val inputField = @Composable {
                HomeTopBarInputField(
                    searchBarState = searchBarState,
                    textFieldState = textFieldState
                )
            }
            TopSearchBar(
                state = searchBarState,
                inputField = inputField,
                scrollBehavior = scrollBehavior
            )
            HomeExpandedTopBar(
                searchBarState = searchBarState,
                inputField = inputField
            )
        },
        bottomBar = {
            val playbackBarState by playbackBarViewModel.playbackBarState.collectAsStateWithLifecycle()
            PlaybackBar(
                state = playbackBarState,
                onAction = { action ->
                    when (action) {
                        PlaybackBarAction.GoToPlaybackRoute -> onGoToPlaybackRoute()
                        PlaybackBarAction.Pause -> homeViewModel.pause()
                        PlaybackBarAction.Play -> homeViewModel.play()
                    }
                }
            )
        }
    ) { contentPadding ->
        contentPadding
    }
}