package org.singularux.music.feature.home.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.singularux.music.core.ui.MusicTheme
import kotlin.time.Duration.Companion.seconds

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    trackItemDataList: List<TrackItemData>,
    onTrackItemAction: (index: Int, data: TrackItemData, action: TrackItemAction) -> Unit
) {
    val currentLayoutDirection = LocalLayoutDirection.current
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = contentPadding.calculateStartPadding(currentLayoutDirection),
            end = contentPadding.calculateEndPadding(currentLayoutDirection),
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding() + 8.dp
        )
    ) {
        itemsIndexed(
            items = trackItemDataList,
            key = { index, item -> item.id }
        ) { index, trackItemData ->
            TrackItem(
                data = trackItemData,
                onAction = { onTrackItemAction(index, trackItemData, it) }
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeContentPreview() {
    MusicTheme {
        HomeContent(
            contentPadding = PaddingValues(vertical = 8.dp),
            trackItemDataList = List(50) {
                TrackItemData(
                    id = it.toLong(),
                    title = "Track $it",
                    artistsName = if (it % 3 == 0) null else "Artist $it",
                    duration = (100 + it).seconds,
                    artworkUri = null,
                    isCurrentlyPlaying = false
                )
            },
            onTrackItemAction = { index, data, action ->  }
        )
    }
}