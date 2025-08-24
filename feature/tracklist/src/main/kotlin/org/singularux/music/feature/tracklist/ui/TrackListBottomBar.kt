package org.singularux.music.feature.tracklist.ui

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.feature.tracklist.R

sealed class TrackListBottomBarData {
    data object Idle : TrackListBottomBarData()
    data class Playing(
        val title: String,
        val artistName: String?,
        val artworkUri: Uri?,
        val progress: Float,
        val isPlaying: Boolean
    ) : TrackListBottomBarData()
}

enum class TrackListBottomBarAction {
    PLAY, PAUSE  // TODO: Implement GO_TO_PLAYBACK_SCREEN
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TrackListBottomBar(
    modifier: Modifier = Modifier,
    data: TrackListBottomBarData,
    onAction: (action: TrackListBottomBarAction) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = {
                when (data) {
                    is TrackListBottomBarData.Idle -> 0.0F
                    is TrackListBottomBarData.Playing -> data.progress
                }
            }
        )
        ListItem(
            headlineContent = {
                val text = when (data) {
                    is TrackListBottomBarData.Idle -> stringResource(R.string.tracklist_bottom_bar_no_track)
                    is TrackListBottomBarData.Playing -> data.title
                }
                Text(text = text)
            },
            supportingContent = {
                val text = when (data) {
                    is TrackListBottomBarData.Playing -> when (data.artistName) {
                        null -> stringResource(R.string.track_item_unknown_artist)
                        else -> data.artistName
                    }
                    is TrackListBottomBarData.Idle -> stringResource(R.string.tracklist_bottom_bar_no_artist)
                }
                Text(text = text)
            },
            leadingContent = {
                AsyncImage(
                    modifier = Modifier.size(56.dp)
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(color = MaterialTheme.colorScheme.surfaceContainerHighest),
                    model = when (data) {
                        is TrackListBottomBarData.Idle -> null
                        is TrackListBottomBarData.Playing -> data.artworkUri
                    },
                    contentDescription = when (data) {
                        is TrackListBottomBarData.Idle -> null
                        is TrackListBottomBarData.Playing -> data.artistName
                    },
                )
            },
            trailingContent = {
                if (data is TrackListBottomBarData.Playing) {
                    FilledIconButton(
                        modifier = Modifier.minimumInteractiveComponentSize()
                            .size(size = IconButtonDefaults.smallContainerSize(
                                widthOption = IconButtonDefaults.IconButtonWidthOption.Wide
                            )),
                        onClick = {
                            if (data.isPlaying) {
                                onAction(TrackListBottomBarAction.PAUSE)
                            } else {
                                onAction(TrackListBottomBarAction.PLAY)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (data.isPlaying) {
                                Icons.Rounded.Pause
                            } else {
                                Icons.Rounded.PlayArrow
                            },
                            contentDescription = stringResource(
                                id = if (data.isPlaying) {
                                    R.string.tracklist_bottom_bar_pause
                                } else {
                                    R.string.tracklist_bottom_bar_play
                                }
                            )
                        )
                    }
                }
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                headlineColor = MaterialTheme.colorScheme.onSurfaceVariant,
                supportingColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewIdle() {
    MusicTheme {
        TrackListBottomBar(
            data = TrackListBottomBarData.Idle,
            onAction = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPlaying() {
    MusicTheme {
        TrackListBottomBar(
            data = TrackListBottomBarData.Playing(
                title = "In The End",
                artistName = "Linkin Park",
                artworkUri = null,
                progress = 0.21F,
                isPlaying = true
            ),
            onAction = {}
        )
    }
}