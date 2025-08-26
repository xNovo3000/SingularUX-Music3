package org.singularux.music.feature.nowplaying.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.feature.nowplaying.viewmodel.NowPlayingViewModel
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingRoute(
    viewModel: NowPlayingViewModel,
    onGoBack: () -> Unit
) {
    Scaffold(
        topBar = {
            NowPlayingTopBar(onGoBack = onGoBack)
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Spacer(modifier = Modifier.weight(1F))
            NowPlayingItemArtwork(
                modifier = Modifier
                    .weight(4F)
                    .align(Alignment.CenterHorizontally),
                artworkUri = null
            )
            Spacer(modifier = Modifier.weight(1F))
            NowPlayingItemTitle(
                modifier = Modifier.padding(horizontal = 28.dp),
                data = NowPlayingItemTitleData(
                    title = "Test",
                    artistName = "Test"
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            NowPlayingItemScrubber(
                modifier = Modifier.padding(horizontal = 20.dp),
                state = viewModel.sliderState,
                data = NowPlayingItemScrubberData(duration = 216.seconds)
            )
            Spacer(modifier = Modifier.height(16.dp))
            NowPlayingItemButtons(
                modifier = Modifier
                    .padding(horizontal = 28.dp)
                    .align(Alignment.CenterHorizontally),
                data = NowPlayingItemButtonsData.Idle,
                onAction = {}
            )
            Spacer(modifier = Modifier.weight(1F))
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
private fun Preview() {
    MusicTheme {
        NowPlayingRoute(
            viewModel = NowPlayingViewModel(),
            onGoBack = {}
        )
    }
}