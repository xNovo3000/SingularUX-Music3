package org.singularux.music.feature.tracklist.ui

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.PlaylistAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.feature.tracklist.R
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class TrackItemData(
    val id: Long,
    val title: String,
    val artistName: String?,
    val duration: Duration,
    val artworkUri: Uri?,
    val isCurrentlyPlaying: Boolean
)

enum class TrackItemAction {
    PLAY, ADD_TO_QUEUE
}

@Composable
fun TrackItem(
    modifier: Modifier = Modifier,
    colors: ListItemColors = ListItemDefaults.colors(),
    data: TrackItemData,
    onAction: (action: TrackItemAction) -> Unit
) {
    val state = rememberSwipeToDismissBoxState()
    LaunchedEffect(state.settledValue) {
        onAction(TrackItemAction.ADD_TO_QUEUE)
        state.reset()
    }
    SwipeToDismissBox(
        modifier = modifier,
        state = state,
        backgroundContent = { TrackItemBackground() }
    ) {
        TrackItemContent(
            colors = colors,
            data = data,
            onAction = onAction
        )
    }
}

@Composable
private fun TrackItemBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.CenterStart
    ) {
        Icon(
            modifier = Modifier.padding(start = 16.dp),
            imageVector = Icons.AutoMirrored.Rounded.PlaylistAdd,
            contentDescription = stringResource(R.string.track_item_add_to_queue),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun TrackItemContent(
    modifier: Modifier = Modifier,
    colors: ListItemColors,
    data: TrackItemData,
    onAction: (action: TrackItemAction) -> Unit
) {
    ListItem(
        modifier = modifier
            .clickable(onClick = { onAction(TrackItemAction.PLAY) }),
        headlineContent = {
            Text(
                text = data.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            val result = stringResource(
                id = R.string.track_item_supporting_content,
                data.duration.inWholeMinutes,
                data.duration.inWholeSeconds % 60,
                data.artistName ?: stringResource(R.string.track_item_unknown_artist)
            )
            Text(
                text = result,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            AsyncImage(
                modifier = Modifier.size(56.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(color = MaterialTheme.colorScheme.surfaceContainerHighest),
                model = data.artworkUri,
                contentDescription = data.title,
            )
        },
        colors = when {
            data.isCurrentlyPlaying -> ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                headlineColor = MaterialTheme.colorScheme.onPrimaryContainer,
                supportingColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
            else -> colors
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    MusicTheme {
        TrackItem(
            data = TrackItemData(
                id = 0,
                title = "In The End",
                artistName = "Linkin Park",
                duration = 216.seconds,
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
private fun PreviewCurrentlyPlaying() {
    MusicTheme {
        TrackItem(
            data = TrackItemData(
                id = 0,
                title = "In The End",
                artistName = "Linkin Park",
                duration = 216.seconds,
                artworkUri = null,
                isCurrentlyPlaying = true
            ),
            onAction = {}
        )
    }
}