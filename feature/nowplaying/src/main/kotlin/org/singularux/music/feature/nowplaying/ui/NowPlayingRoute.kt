package org.singularux.music.feature.nowplaying.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            val nowPlayingItemArtworkData by viewModel.nowPlayingItemArtworkData.collectAsStateWithLifecycle()
            NowPlayingItemArtwork(
                modifier = Modifier
                    .weight(4F)
                    .align(Alignment.CenterHorizontally),
                data = nowPlayingItemArtworkData
            )
            Spacer(modifier = Modifier.weight(1F))
            val nowPlayingItemTitleData by viewModel.nowPlayingItemTitleData.collectAsStateWithLifecycle()
            NowPlayingItemTitle(
                modifier = Modifier.padding(horizontal = 28.dp),
                data = nowPlayingItemTitleData
            )
            Spacer(modifier = Modifier.height(16.dp))
            val nowPlayingItemScrubberData by viewModel.nowPlayingItemScrubberData.collectAsStateWithLifecycle()
            NowPlayingItemScrubber(
                modifier = Modifier.padding(horizontal = 20.dp),
                data = nowPlayingItemScrubberData,
                onSeek = { viewModel.seekTo(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            val nowPlayingItemButtonsData by viewModel.nowPlayingItemButtonsData.collectAsStateWithLifecycle()
            NowPlayingItemButtons(
                modifier = Modifier
                    .padding(horizontal = 28.dp)
                    .align(Alignment.CenterHorizontally),
                data = nowPlayingItemButtonsData,
                onAction = {
                    when (it) {
                        NowPlayingItemButtonsAction.PLAY -> viewModel.play()
                        NowPlayingItemButtonsAction.PAUSE -> viewModel.pause()
                        NowPlayingItemButtonsAction.SKIP_PREV -> viewModel.skipPrev()
                        NowPlayingItemButtonsAction.SKIP_NEXT -> viewModel.skipNext()
                    }
                }
            )
            Spacer(modifier = Modifier.weight(1F))
        }
    }
}