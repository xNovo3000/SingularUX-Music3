package org.singularux.music.feature.home.ui

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import org.singularux.music.feature.home.R
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class TrackItemData(
    val id: Long,
    val title: String,
    val artistsName: String?,
    val duration: Duration,
    val artworkUri: Uri?,
    val isCurrentlyPlaying: Boolean
)

sealed interface TrackItemAction {
    data object Play : TrackItemAction
    data object AddToQueue : TrackItemAction
}

@Composable
fun TrackItem(
    modifier: Modifier = Modifier,
    data: TrackItemData,
    onAction: (TrackItemAction) -> Unit
) {
    ListItem(
        modifier = modifier
            .clickable(onClick = { onAction(TrackItemAction.Play) }),
        headlineContent = {
            Text(
                text = data.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            val artistsName = data.artistsName ?: stringResource(R.string.track_item_unknown_artist)
            val durationMinutes = data.duration.inWholeMinutes
            val durationSeconds = data.duration.inWholeSeconds % 60
            val text = stringResource(R.string.track_item_supporting,
                durationMinutes, durationSeconds, artistsName)
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            AsyncImage(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data.artworkUri)
                    .crossfade(300)
                    .size(R.dimen.track_item_artwork_size)
                    .build(),
                contentDescription = stringResource(R.string.track_item_artwork_description)
            )
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TrackItemPreview() {
    MusicTheme {
        TrackItem(
            data = TrackItemData(
                id = 1,
                title = "In The End",
                artistsName = "Linkin Park",
                duration = 203.seconds,
                artworkUri = null,
                isCurrentlyPlaying = false
            ),
            onAction = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TrackItemNoArtistPreview() {
    MusicTheme {
        TrackItem(
            data = TrackItemData(
                id = 1,
                title = "In The End",
                artistsName = null,
                duration = 203.seconds,
                artworkUri = null,
                isCurrentlyPlaying = false
            ),
            onAction = {}
        )
    }
}

data class SearchTrackItemData(
    val id: Long,
    val title: String,
    val artistsName: String?,
    val duration: Duration,
    val artworkUri: Uri?
)

sealed interface SearchTrackItemAction {
    data object Play : SearchTrackItemAction
    data object AddToQueue : SearchTrackItemAction
}

@Composable
fun SearchTrackItem(
    modifier: Modifier = Modifier,
    data: SearchTrackItemData,
    onAction: (SearchTrackItemAction) -> Unit
) {
    ListItem(
        modifier = modifier
            .clickable(onClick = { onAction(SearchTrackItemAction.Play) }),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        headlineContent = {
            Text(
                text = data.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            val artistsName = data.artistsName ?: stringResource(R.string.track_item_unknown_artist)
            val durationMinutes = data.duration.inWholeMinutes
            val durationSeconds = data.duration.inWholeSeconds % 60
            val text = stringResource(R.string.track_item_supporting,
                durationMinutes, durationSeconds, artistsName)
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            AsyncImage(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data.artworkUri)
                    .crossfade(300)
                    .size(R.dimen.track_item_artwork_size)
                    .build(),
                contentDescription = stringResource(R.string.track_item_artwork_description)
            )
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchTrackItemPreview() {
    MusicTheme {
        SearchTrackItem(
            data = SearchTrackItemData(
                id = 1,
                title = "In The End",
                artistsName = "Linkin Park",
                duration = 203.seconds,
                artworkUri = null
            ),
            onAction = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchTrackItemNoArtistPreview() {
    MusicTheme {
        SearchTrackItem(
            data = SearchTrackItemData(
                id = 1,
                title = "In The End",
                artistsName = null,
                duration = 203.seconds,
                artworkUri = null
            ),
            onAction = {}
        )
    }
}