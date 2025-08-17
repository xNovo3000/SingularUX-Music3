package org.singularux.music.feature.tracklist.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.feature.tracklist.viewmodel.TrackListViewModel

@Composable
fun TrackListRoute(viewModel: TrackListViewModel) {
    val contentState = rememberLazyListState()
    Scaffold(
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
            TrackListBottomBar(
                data = TrackListBottomBarData.Idle,
                onAction = {}
            )
        }
    ) { innerPadding ->
        innerPadding
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    MusicTheme {
        TrackListRoute(viewModel = TrackListViewModel())
    }
}