package org.singularux.music.feature.nowplaying.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.feature.nowplaying.R
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class NowPlayingItemScrubberData(
    val duration: Duration
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingItemScrubber(
    modifier: Modifier = Modifier,
    state: SliderState,
    data: NowPlayingItemScrubberData
) {
    Column(modifier = modifier) {
        Slider(state = state)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val progress by remember(state.value, data.duration) {
                derivedStateOf { data.duration * state.value.toDouble() }
            }
            Text(
                text = stringResource(
                    id = R.string.now_playing_item_scrubber_duration,
                    progress.inWholeSeconds / 60,
                    progress.inWholeSeconds % 60
                ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(
                    id = R.string.now_playing_item_scrubber_duration,
                    data.duration.inWholeSeconds / 60,
                    data.duration.inWholeSeconds % 60
                ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Preview() {
    MusicTheme {
        Surface {
            NowPlayingItemScrubber(
                state = rememberSliderState(value = 0.15F),
                data = NowPlayingItemScrubberData(
                    duration = 216.seconds
                )
            )
        }
    }
}