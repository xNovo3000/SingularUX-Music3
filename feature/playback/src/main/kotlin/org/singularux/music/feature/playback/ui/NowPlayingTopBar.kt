package org.singularux.music.feature.playback.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.feature.playback.R

@ExperimentalMaterial3ExpressiveApi
@ExperimentalMaterial3Api
@Composable
fun NowPlayingTopBar(onBackPress: () -> Unit) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            FilledTonalIconButton(
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .size(
                        size = IconButtonDefaults.smallContainerSize(
                            widthOption = IconButtonDefaults.IconButtonWidthOption.Narrow
                        )
                    ),
                onClick = onBackPress,
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.now_playing_app_bar_back)
                )
            }
        },
        title = {
            Text(text = stringResource(R.string.now_playing_app_bar_title))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@ExperimentalMaterial3ExpressiveApi
@ExperimentalMaterial3Api
@Composable
private fun NowPlayingTopBarPreview() {
    MusicTheme {
        NowPlayingTopBar {}
    }
}