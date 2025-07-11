package org.singularux.music.feature.home.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    trackItemDataList: List<TrackItemData>,
    onTrackItemAction: (TrackItemData, TrackItemAction) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(
            items = trackItemDataList,
            key = { it.id }
        ) { trackItemData ->
            TrackItem(
                data = trackItemData,
                onAction = { onTrackItemAction(trackItemData, it) }
            )
        }
    }
}