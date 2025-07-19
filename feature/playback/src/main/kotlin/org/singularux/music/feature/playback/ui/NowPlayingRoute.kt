package org.singularux.music.feature.playback.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.singularux.music.feature.playback.viewmodel.NowPlayingViewModel

@ExperimentalMaterial3ExpressiveApi
@ExperimentalMaterial3Api
@Composable
fun NowPlayingRoute(
    nowPlayingViewModel: NowPlayingViewModel,
    onBackPress: () -> Unit
) {
    Scaffold(
        topBar = {
            NowPlayingTopBar(onBackPress = onBackPress)
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .navigationBarsPadding()
        ) {
            Spacer(modifier = Modifier.weight(1F))
            NowPlayingArtwork(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                artworkUri = null
            )
            Spacer(modifier = Modifier.weight(1F))
            NowPlayingTitleArtists(
                modifier = Modifier.padding(horizontal = 16.dp),
                data = NowPlayingTitleArtistsData(
                    title = null,
                    artist = null
                )
            )
            Spacer(modifier = Modifier.size(16.dp))
            // TODO: Slider
            Spacer(modifier = Modifier.size(4.dp))
            // TODO: Start and end time
            Spacer(modifier = Modifier.size(24.dp))
            // TODO: Buttons
            Spacer(modifier = Modifier.weight(2F))
        }
    }
}