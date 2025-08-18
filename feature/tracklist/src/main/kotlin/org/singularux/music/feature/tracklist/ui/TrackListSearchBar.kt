package org.singularux.music.feature.tracklist.ui

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.feature.tracklist.R
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackListSearchBarInputField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState,
    searchBarState: SearchBarState
) {
    val placeholderText = stringResource(R.string.tracklist_search_placeholder)
    val coroutineScope = rememberCoroutineScope()
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    SearchBarDefaults.InputField(
        modifier = modifier,
        textFieldState = textFieldState,
        searchBarState = searchBarState,
        onSearch = {
            softwareKeyboardController?.hide()
        },
        placeholder = {
            Text(text = placeholderText)
        },
        leadingIcon = {
            when (searchBarState.currentValue) {
                SearchBarValue.Collapsed -> {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = placeholderText
                    )
                }
                SearchBarValue.Expanded -> {
                    IconButton(
                        onClick = {
                            textFieldState.setTextAndPlaceCursorAtEnd("")
                            coroutineScope.launch { searchBarState.animateToCollapsed() }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.tracklist_search_back)
                        )
                    }
                }
            }
        },
        trailingIcon = {
            if (searchBarState.currentValue == SearchBarValue.Expanded) {
                IconButton(
                    onClick = {
                        textFieldState.setTextAndPlaceCursorAtEnd("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = stringResource(R.string.tracklist_search_clear)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackListSearchBarCollapsed(
    modifier: Modifier = Modifier,
    state: SearchBarState,
    inputField: @Composable () -> Unit,
    scrollBehavior: SearchBarScrollBehavior? = null
) {
    AppBarWithSearch(
        modifier = modifier,
        state = state,
        inputField = inputField,
        scrollBehavior = scrollBehavior
    )
    val surfaceColor = MaterialTheme.colorScheme.surface
    val calculatedHeight = WindowInsets.statusBars.getTop(LocalDensity.current).toFloat()
    Canvas(modifier = Modifier.fillMaxWidth()) {
        drawRect(
            color = surfaceColor,
            size = Size(size.width, calculatedHeight),
            alpha = 0.75F,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackListSearchBarExpanded(
    modifier: Modifier = Modifier,
    state: SearchBarState,
    inputField: @Composable () -> Unit,
    items: List<TrackItemData>,
    onItemAction: (index: Int, action: TrackItemAction) -> Unit
) {
    ExpandedFullScreenSearchBar(
        modifier = modifier,
        state = state,
        inputField = inputField
    ) {
        LazyColumn {
            itemsIndexed(
                items = items,
                key = { _, item -> item.id }
            ) { index, item ->
                TrackItem(
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    data = item,
                    onAction = { onItemAction(index, it) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewExpanded() {
    MusicTheme {
        val searchBarState = rememberSearchBarState(SearchBarValue.Expanded)
        TrackListSearchBarExpanded(
            state = searchBarState,
            inputField = {
                TrackListSearchBarInputField(
                    textFieldState = rememberTextFieldState(),
                    searchBarState = searchBarState
                )
            },
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
            onItemAction = { index, action -> }
        )
    }
}