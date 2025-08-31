package org.singularux.music.feature.tracklist.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.singularux.music.core.ui.MusicSurface
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.feature.tracklist.R
import kotlin.time.Duration.Companion.seconds

@Composable
fun TrackListContent(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues,
    items: List<TrackItemData>,
    onItemAction: (index: Int, item: TrackItemData, action: TrackItemAction) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding
    ) {
        itemsIndexed(
            items = items,
            key = { _, item -> item.id },
            contentType = { _, type -> type }
        ) { index, item ->
            TrackItem(
                modifier = Modifier.testTag("track_item_$index"),
                data = item,
                onAction = { onItemAction(index, item, it) }
            )
        }
    }
}

@Composable
fun TrackListContentNoPermission(
    modifier: Modifier = Modifier,
    onGivePermissionClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.tracklist_content_no_permission_rationale),
            textAlign = TextAlign.Center
        )
        FilledTonalButton(onClick = onGivePermissionClick) {
            Text(text = stringResource(R.string.tracklist_content_no_permission_button))
        }
    }
}

@Composable
fun TrackListContentNoTracks(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.tracklist_content_no_tracks_rationale),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    MusicTheme {
        MusicSurface {
            TrackListContent(
                contentPadding = PaddingValues(0.dp),
                items = List(50) {
                    TrackItemData(
                        id = it.toLong(),
                        title = "Track $it",
                        artistName = if (it % 2 == 0) {
                            "Artist $it"
                        } else {
                            null
                        },
                        duration = it.seconds,
                        artworkUri = null,
                        isCurrentlyPlaying = it == 4
                    )
                },
                onItemAction = { index, item, action -> }
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewNoPermission() {
    MusicTheme {
        MusicSurface {
            TrackListContentNoPermission(
                onGivePermissionClick = {}
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewNoTracks() {
    MusicTheme {
        MusicSurface {
            TrackListContentNoTracks()
        }
    }
}