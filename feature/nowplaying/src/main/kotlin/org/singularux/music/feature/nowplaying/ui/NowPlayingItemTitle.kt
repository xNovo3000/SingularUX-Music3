package org.singularux.music.feature.nowplaying.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.feature.nowplaying.R

sealed class NowPlayingItemTitleData {
    data object Idle : NowPlayingItemTitleData()
    data class Playing(
        val title: String,
        val artistName: String?
    ) : NowPlayingItemTitleData()
}

@Composable
fun NowPlayingItemTitle(
    modifier: Modifier = Modifier,
    data: NowPlayingItemTitleData
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = when (data) {
                is NowPlayingItemTitleData.Idle -> stringResource(R.string.now_playing_item_title_no_track)
                is NowPlayingItemTitleData.Playing -> data.title
            },
            style = MaterialTheme.typography.headlineLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = when (data) {
                is NowPlayingItemTitleData.Idle -> stringResource(R.string.now_playing_item_title_no_artist)
                is NowPlayingItemTitleData.Playing -> data.artistName ?: stringResource(R.string.now_playing_item_title_unknown_artist)
            },
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewIdle() {
    MusicTheme {
        Surface {
            NowPlayingItemTitle(data = NowPlayingItemTitleData.Idle)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPlaying() {
    MusicTheme {
        Surface {
            NowPlayingItemTitle(
                data = NowPlayingItemTitleData.Playing(
                    title = "In The End",
                    artistName = "Linkin Park"
                )
            )
        }
    }
}