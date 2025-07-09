package org.singularux.music.feature.playback.ui

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.feature.playback.R

data class PlaybackBarState(
    val title: String,
    val artistsName: String?,
    val artworkUri: Uri?,
    val isPlaying: Boolean,
    val progress: Float
)

sealed interface PlaybackBarAction {
    data object PlayPause : PlaybackBarAction
    data object GoToPlaybackScreen : PlaybackBarAction
}

@ExperimentalMaterial3ExpressiveApi
@Composable
fun PlaybackBar(
    modifier: Modifier = Modifier,
    state: PlaybackBarState,
    onAction: (PlaybackBarAction) -> Unit
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .clickable(
                onClick = {
                    onAction(PlaybackBarAction.GoToPlaybackScreen)
                }
            )
    ) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = { state.progress }
        )
        ListItem(
            modifier = Modifier.fillMaxWidth(),
            headlineContent = {
                Text(text = state.title)
            },
            supportingContent = {
                val supportText = state.artistsName
                    ?: stringResource(R.string.playback_bar_unknown_artist)
                Text(text = supportText)
            },
            leadingContent = {
                AsyncImage(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.artworkUri)
                        .crossfade(300)
                        .size(R.dimen.playback_bar_artwork_size)
                        .build(),
                    contentDescription = stringResource(R.string.playback_bar_artwork_description)
                )
            },
            trailingContent = {
                FilledIconButton(
                    modifier = Modifier.size(
                        size = IconButtonDefaults.smallContainerSize(
                            widthOption = IconButtonDefaults.IconButtonWidthOption.Wide
                        )
                    ),
                    onClick = {
                        onAction(PlaybackBarAction.PlayPause)
                    },
                    shapes = IconButtonDefaults.shapes(
                        shape = IconButtonDefaults.smallRoundShape,
                        pressedShape = IconButtonDefaults.smallSquareShape
                    )
                ) {
                    Icon(
                        imageVector = if (state.isPlaying) {
                            Icons.Rounded.Pause
                        } else {
                            Icons.Rounded.PlayArrow
                        },
                        contentDescription = stringResource(
                            id = if (state.isPlaying) {
                                R.string.playback_bar_pause
                            } else {
                                R.string.playback_bar_play
                            }
                        )
                    )
                }
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@ExperimentalMaterial3ExpressiveApi
@Composable
private fun Preview() {
    MusicTheme {
        PlaybackBar(
            state = PlaybackBarState(
                title = "In The End",
                artistsName = "Linkin Park",
                artworkUri = null,
                isPlaying = true,
                progress = 0.12F
            ),
            onAction = {}
        )
    }
}