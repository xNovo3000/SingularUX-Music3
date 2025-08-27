package org.singularux.music.feature.nowplaying.ui

import android.content.res.Configuration
import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.feature.nowplaying.R
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class NowPlayingItemScrubberData(
    @param:FloatRange(from = 0.0, to = 1.0) val progress: Float,
    val duration: Duration
)

@Composable
fun NowPlayingItemScrubber(
    modifier: Modifier = Modifier,
    data: NowPlayingItemScrubberData,
    onSeek: (Duration) -> Unit
) {
    Column(modifier = modifier) {
        var sliderPosition by remember { mutableFloatStateOf(data.progress) }
        var isSliding by remember { mutableStateOf(false) }
        Slider(
            value = if (isSliding) sliderPosition else data.progress,
            onValueChange = {
                sliderPosition = it
                isSliding = true
            },
            onValueChangeFinished = {
                onSeek(data.duration * sliderPosition.toDouble())
                isSliding = false
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val progress by remember(data, sliderPosition) {
                derivedStateOf {
                    val progress = if (isSliding) sliderPosition else data.progress
                    data.duration * progress.toDouble()
                }
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
@Composable
private fun Preview() {
    MusicTheme {
        Surface {
            NowPlayingItemScrubber(
                data = NowPlayingItemScrubberData(
                    progress = 0.15F,
                    duration = 216.seconds
                ),
                onSeek = {}
            )
        }
    }
}