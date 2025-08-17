package org.singularux.music.feature.tracklist.ui

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.feature.tracklist.R

@Composable
fun TrackListFab(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onClick: () -> Unit,
) {
    val text = stringResource(R.string.tracklist_shuffle)
    ExtendedFloatingActionButton(
        modifier = modifier,
        expanded = expanded,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.Rounded.Shuffle,
                contentDescription = text
            )
        },
        text = {
            Text(text = text)
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    MusicTheme {
        TrackListFab(
            expanded = false,
            onClick = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewExpanded() {
    MusicTheme {
        TrackListFab(
            expanded = true,
            onClick = {}
        )
    }
}