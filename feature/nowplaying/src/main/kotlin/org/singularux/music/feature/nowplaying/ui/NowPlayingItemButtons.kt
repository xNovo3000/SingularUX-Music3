package org.singularux.music.feature.nowplaying.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.feature.nowplaying.R

sealed class NowPlayingItemButtonsData {
    data object Idle : NowPlayingItemButtonsData()
    data class Playing(
        val isPlaying: Boolean,
        val hasNext: Boolean
    ) : NowPlayingItemButtonsData()
}

enum class NowPlayingItemButtonsAction {
    PLAY, PAUSE, SKIP_PREV, SKIP_NEXT
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NowPlayingItemButtons(
    modifier: Modifier = Modifier,
    data: NowPlayingItemButtonsData,
    onAction: (NowPlayingItemButtonsAction) -> Unit
) {
    Row(modifier = modifier) {
        FilledTonalIconButton(
            modifier = Modifier.minimumInteractiveComponentSize()
                .size(size = IconButtonDefaults.mediumContainerSize(
                    widthOption = IconButtonDefaults.IconButtonWidthOption.Narrow
                )),
            onClick = { onAction(NowPlayingItemButtonsAction.SKIP_PREV) },
            enabled = data is NowPlayingItemButtonsData.Playing
        ) {
            Icon(
                imageVector = Icons.Rounded.SkipPrevious,
                contentDescription = stringResource(R.string.now_playing_item_buttons_skip_prev)
            )
        }
        FilledTonalIconButton(
            modifier = Modifier.minimumInteractiveComponentSize()
                .size(size = IconButtonDefaults.mediumContainerSize(
                    widthOption = IconButtonDefaults.IconButtonWidthOption.Narrow
                )),
            onClick = { onAction(NowPlayingItemButtonsAction.SKIP_NEXT) },
            enabled = data is NowPlayingItemButtonsData.Playing && data.hasNext
        ) {
            Icon(
                imageVector = Icons.Rounded.SkipNext,
                contentDescription = stringResource(R.string.now_playing_item_buttons_skip_next)
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewIdle() {
    MusicTheme {
        Surface {
            NowPlayingItemButtons(
                data = NowPlayingItemButtonsData.Idle,
                onAction = {}
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPlaying() {
    MusicTheme {
        Surface {
            NowPlayingItemButtons(
                data = NowPlayingItemButtonsData.Playing(
                    isPlaying = true,
                    hasNext = true
                ),
                onAction = {}
            )
        }
    }
}