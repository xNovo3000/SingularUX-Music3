package org.singularux.music.feature.home.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.FlowPreview
import org.singularux.music.feature.home.viewmodel.HomeViewModel
import org.singularux.music.feature.playback.ui.PlaybackBar
import org.singularux.music.feature.playback.ui.PlaybackBarAction
import org.singularux.music.feature.playback.viewmodel.PlaybackBarViewModel

@ExperimentalMaterial3ExpressiveApi
@ExperimentalMaterial3Api
@ExperimentalPermissionsApi
@FlowPreview
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
            val searchBarState = rememberSearchBarState()
            val inputField = @Composable {
                HomeTopBarInputField(
                    searchBarState = searchBarState,
                    textFieldState = homeViewModel.searchTextFieldState
                )
            }
            TopSearchBar(
                state = searchBarState,
                inputField = inputField,
                scrollBehavior = scrollBehavior
            )
            val searchTrackItemDataList by homeViewModel.trackItemDataSearchList.collectAsStateWithLifecycle()
            HomeExpandedTopBar(
                searchBarState = searchBarState,
                inputField = inputField,
                searchTrackItemDataList = searchTrackItemDataList,
                onSearchTrackItemAction = { data, action ->
                    // TODO
                }
            )
        },
        bottomBar = {
            val playbackBarState by playbackBarViewModel.playbackBarState.collectAsStateWithLifecycle()
            PlaybackBar(
                state = playbackBarState,
                onAction = { action ->
                    when (action) {
                        PlaybackBarAction.GoToPlaybackRoute -> onGoToPlaybackRoute()
                        PlaybackBarAction.Pause -> playbackBarViewModel.pause()
                        PlaybackBarAction.Play -> playbackBarViewModel.play()
                    }
                }
            )
        }
    ) { contentPadding ->
        val readMusicPermissionState = rememberPermissionState(homeViewModel.readMusicPermission)
        if (readMusicPermissionState.status.isGranted) {
            val trackItemDataList by homeViewModel.trackItemDataList.collectAsStateWithLifecycle()
            HomeContent(
                contentPadding = contentPadding,
                trackItemDataList = trackItemDataList,
                onTrackItemAction = { data, action ->
                    // TODO
                }
            )
        }
    }
}