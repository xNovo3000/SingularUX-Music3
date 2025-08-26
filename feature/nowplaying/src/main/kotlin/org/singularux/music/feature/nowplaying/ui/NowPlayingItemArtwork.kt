package org.singularux.music.feature.nowplaying.ui

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.feature.nowplaying.R

@Composable
fun NowPlayingItemArtwork(
    modifier: Modifier = Modifier,
    artworkUri: Uri?
) {
    AsyncImage(
        modifier = modifier
            .aspectRatio(1F)
            .fillMaxSize()
            .clip(shape = MaterialTheme.shapes.extraLarge)
            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest),
        model = artworkUri,
        contentDescription = stringResource(R.string.now_playing_item_artwork)
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    MusicTheme {
        NowPlayingItemArtwork(artworkUri = null)
    }
}