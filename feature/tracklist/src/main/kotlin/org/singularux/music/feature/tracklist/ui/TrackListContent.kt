package org.singularux.music.feature.tracklist.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.singularux.music.core.ui.MusicSurface
import org.singularux.music.core.ui.MusicTheme
import kotlin.time.Duration.Companion.seconds

@Composable
fun TrackListContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    items: List<TrackItemData>,
    onItemAction: (TrackItemData, TrackItemAction) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(
            items = items,
            key = { it.id }
        ) { trackItemData ->
            TrackItem(
                data = trackItemData,
                onAction = { onItemAction(trackItemData, it) }
            )
        }
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
                onItemAction = { data, action ->

                }
            )
        }
    }
}