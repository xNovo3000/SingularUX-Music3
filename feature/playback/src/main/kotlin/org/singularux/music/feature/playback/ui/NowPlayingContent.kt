package org.singularux.music.feature.playback.ui

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.feature.playback.R
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun NowPlayingArtwork(
    modifier: Modifier = Modifier,
    artworkUri: Uri?
) {
    AsyncImage(
        modifier = modifier
            .size(280.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        model = ImageRequest.Builder(LocalContext.current)
            .data(artworkUri)
            .size(R.dimen.now_playing_artwork_size)
            .crossfade(300)
            .build(),
        contentDescription = null  // TODO
    )
}

data class NowPlayingTitleArtistsData(
    val title: String?,
    val artist: String?
)

@Composable
fun NowPlayingTitleArtists(
    modifier: Modifier = Modifier,
    data: NowPlayingTitleArtistsData
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = data.title ?: stringResource(R.string.now_playing_unknown_track),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = data.artist ?: stringResource(R.string.now_playing_unknown_artist),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NowPlayingTitleArtistsPreview() {
    MusicTheme {
        Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
            NowPlayingTitleArtists(
                data = NowPlayingTitleArtistsData(
                    title = "In The End",
                    artist = "Linkin Park"
                )
            )
        }
    }
}

data class NowPlayingTimeData(
    val progress: Float,
    val total: Duration
)

@ExperimentalMaterial3Api
@Composable
fun NowPlayingTime(
    modifier: Modifier = Modifier,
    data: NowPlayingTimeData,
    onValueChange: (value: Float) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        var uiProgress by remember { mutableFloatStateOf(0.0F) }
        val sliderState = rememberSliderState(
            onValueChangeFinished = { onValueChange(uiProgress) }
        )
        LaunchedEffect(uiProgress) {
            if (!sliderState.isDragging) {
                sliderState.value = uiProgress
            }
        }
        Slider(state = sliderState)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val current = data.total * data.progress.toDouble()
            Text(
                text = stringResource(
                    R.string.now_playing_duration,
                    current.inWholeMinutes,
                    current.inWholeSeconds % 60
                ),
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = stringResource(
                    R.string.now_playing_duration,
                    data.total.inWholeMinutes,
                    data.total.inWholeSeconds % 60
                ),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@ExperimentalMaterial3Api
@Composable
private fun NowPlayingCurrentEndTimePreview() {
    MusicTheme {
        Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
            NowPlayingTime(
                modifier = Modifier.padding(horizontal = 16.dp),
                data = NowPlayingTimeData(
                    progress = 0.35F,
                    total = 201.seconds
                ),
                onValueChange = {}
            )
        }
    }
}