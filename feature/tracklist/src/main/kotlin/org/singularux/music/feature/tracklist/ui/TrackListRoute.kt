package org.singularux.music.feature.tracklist.ui

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.singularux.music.feature.tracklist.viewmodel.TrackListViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TrackListRoute(viewModel: TrackListViewModel) {
    val contentState = rememberLazyListState()
    val searchBarScrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(searchBarScrollBehavior.nestedScrollConnection),
        topBar = {
            val searchBarState = rememberSearchBarState()
            val inputField = @Composable {
                TrackListSearchBarInputField(
                    textFieldState = viewModel.searchBarTextFieldState,
                    searchBarState = searchBarState
                )
            }
            TrackListSearchBarCollapsed(
                state = searchBarState,
                inputField = inputField,
                scrollBehavior = searchBarScrollBehavior
            )
            TrackListSearchBarExpanded(
                state = searchBarState,
                inputField = inputField,
                items = emptyList(),
                onItemAction = { index, action -> }
            )
        },
        floatingActionButton = {
            val expanded by remember {
                derivedStateOf { contentState.firstVisibleItemIndex == 0 }
            }
            TrackListFab(
                expanded = expanded,
                onClick = {}
            )
        },
        bottomBar = {
            val playbackData by viewModel.playbackData.collectAsStateWithLifecycle()
            TrackListBottomBar(
                data = playbackData,
                onAction = {}
            )
        }
    ) { innerPadding ->
        val readMusicPermissionState = rememberPermissionState(viewModel.readMusicPermission)
        LaunchedEffect(Unit) { readMusicPermissionState.launchPermissionRequest() }
        if (readMusicPermissionState.status.isGranted) {
            val trackList by viewModel.trackList.collectAsStateWithLifecycle()
            TrackListContent(
                contentPadding = innerPadding,
                state = contentState,
                items = trackList,
                onItemAction = { index, action -> }
            )
        } else {
            TrackListContentNoPermission(
                onGivePermissionClick = { /* TODO: Go to the permission screen using intents */ }
            )
        }
    }
    val readPhoneStatePermissionState = rememberPermissionState(viewModel.readPhoneStatePermission)
    LaunchedEffect(Unit) { readPhoneStatePermissionState.launchPermissionRequest() }
    // TODO: Show SnackBar when the user does not want to let this app listen phone calls
}