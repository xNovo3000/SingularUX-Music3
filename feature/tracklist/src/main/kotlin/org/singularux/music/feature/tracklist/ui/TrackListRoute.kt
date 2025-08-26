package org.singularux.music.feature.tracklist.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch
import org.singularux.music.feature.tracklist.R
import org.singularux.music.feature.tracklist.viewmodel.TrackListViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TrackListRoute(viewModel: TrackListViewModel) {
    val contentState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val searchBarScrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarReadPhoneRationaleString = stringResource(R.string.tracklist_snackbar_read_phone_rationale)
    val snackbarReadPhoneActionString = stringResource(R.string.tracklist_snackbar_read_phone_action)
    val snackbarAddedToQueueFeedback = stringResource(R.string.tracklist_snackbar_added_to_queue_feedback)
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
            val searchTrackList by viewModel.searchTrackList.collectAsStateWithLifecycle()
            TrackListSearchBarExpanded(
                state = searchBarState,
                inputField = inputField,
                items = searchTrackList,
                onItemAction = { index, item, action ->
                    when (action) {
                        TrackItemAction.PLAY -> viewModel.playFromSearchTrackList(index)
                        TrackItemAction.ADD_TO_QUEUE -> {
                            viewModel.addToQueue(index)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = snackbarAddedToQueueFeedback
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            val expanded by remember {
                derivedStateOf {
                    contentState.lastScrolledBackward || contentState.firstVisibleItemIndex == 0
                }
            }
            TrackListFab(
                expanded = expanded,
                onClick = { viewModel.playShuffled() }
            )
        },
        bottomBar = {
            val playbackData by viewModel.playbackData.collectAsStateWithLifecycle()
            TrackListBottomBar(
                data = playbackData,
                onAction = { action ->
                    when (action) {
                        TrackListBottomBarAction.PLAY -> viewModel.play()
                        TrackListBottomBarAction.PAUSE -> viewModel.pause()
                    }
                }
            )
        }
    ) { innerPadding ->
        // Permission state holders
        var hasReadMusicPermission by remember { mutableStateOf(false) }
        // Request permissions
        val permissionRequestState = rememberMultiplePermissionsState(
            permissions = listOf(viewModel.readMusicPermission, viewModel.readPhoneStatePermission),
            onPermissionsResult = { result ->
                result.forEach { permission, result ->
                    when (permission) {
                        viewModel.readMusicPermission -> hasReadMusicPermission = result
                        viewModel.readPhoneStatePermission -> if (!result) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = snackbarReadPhoneRationaleString,
                                    actionLabel = snackbarReadPhoneActionString,
                                    withDismissAction = true
                                ).let {
                                    if (it == SnackbarResult.ActionPerformed) {
                                        // TODO: Go to the permission screen using intents
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
        LaunchedEffect(Unit) { permissionRequestState.launchMultiplePermissionRequest() }
        // Show track list only if permission is granted
        if (hasReadMusicPermission) {
            val layoutDirection = LocalLayoutDirection.current
            val trackList by viewModel.trackList.collectAsStateWithLifecycle()
            TrackListContent(
                contentPadding = PaddingValues(
                    start = innerPadding.calculateStartPadding(layoutDirection),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(layoutDirection),
                    bottom = innerPadding.calculateBottomPadding() + 92.dp
                ),
                state = contentState,
                items = trackList,
                onItemAction = { index, item, action ->
                    when (action) {
                        TrackItemAction.PLAY -> viewModel.playFromTrackList(index)
                        TrackItemAction.ADD_TO_QUEUE -> {
                            viewModel.addToQueue(index)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = snackbarAddedToQueueFeedback
                                )
                            }
                        }
                    }
                }
            )
        } else {
            TrackListContentNoPermission(
                onGivePermissionClick = { /* TODO: Go to the permission screen using intents */ }
            )
        }
    }
}