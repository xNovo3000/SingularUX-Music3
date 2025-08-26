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

data class NowPlayingItemTitleData(
    val title: String,
    val artistName: String?
)

@Composable
fun NowPlayingItemTitle(
    modifier: Modifier = Modifier,
    data: NowPlayingItemTitleData
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = data.title,
            style = MaterialTheme.typography.headlineLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = data.artistName ?: stringResource(R.string.now_playing_item_title_unknown_artist),
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
private fun Preview() {
    MusicTheme {
        Surface {
            NowPlayingItemTitle(
                data = NowPlayingItemTitleData(
                    title = "In The End",
                    artistName = "Linkin Park"
                )
            )
        }
    }
}