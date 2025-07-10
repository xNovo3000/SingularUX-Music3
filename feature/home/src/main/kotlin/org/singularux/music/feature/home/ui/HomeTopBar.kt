package org.singularux.music.feature.home.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.singularux.music.core.ui.MusicTheme
import org.singularux.music.feature.home.R

@ExperimentalMaterial3Api
@Composable
fun HomeTopBarInputField(
    modifier: Modifier = Modifier,
    searchBarState: SearchBarState,
    textFieldState: TextFieldState
) {
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    SearchBarDefaults.InputField(
        modifier = modifier,
        textFieldState = textFieldState,
        searchBarState = searchBarState,
        onSearch = { keyboardController?.hide() },
        placeholder = {
            Text(text = stringResource(R.string.search_bar_placeholder))
        },
        leadingIcon = {
            if (searchBarState.currentValue == SearchBarValue.Expanded) {
                IconButton(
                    onClick = {
                        textFieldState.setTextAndPlaceCursorAtEnd("")
                        coroutineScope.launch { searchBarState.animateToCollapsed() }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.search_bar_back)
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.search_bar_search)
                )
            }
        },
        trailingIcon = {
            if (searchBarState.currentValue == SearchBarValue.Expanded) {
                IconButton(
                    onClick = { textFieldState.setTextAndPlaceCursorAtEnd("") }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.search_bar_empty)
                    )
                }
            }
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@ExperimentalMaterial3Api
@Composable
private fun HomeTopBarInputFieldPreview() {
    MusicTheme {
        HomeTopBarInputField(
            textFieldState = rememberTextFieldState(),
            searchBarState = rememberSearchBarState(SearchBarValue.Collapsed)
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun HomeExpandedTopBar(
    modifier: Modifier = Modifier,
    searchBarState: SearchBarState,
    inputField: @Composable () -> Unit
) {
    ExpandedFullScreenSearchBar(
        modifier = modifier,
        state = searchBarState,
        inputField = inputField
    ) {
        LazyColumn {}
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@ExperimentalMaterial3Api
@Composable
private fun HomeTopBarPreview() {
    MusicTheme {
        val searchBarState = rememberSearchBarState(SearchBarValue.Collapsed)
        val inputField = @Composable {
            HomeTopBarInputField(
                textFieldState = rememberTextFieldState(),
                searchBarState = searchBarState
            )
        }
        Box {
            TopSearchBar(
                state = searchBarState,
                inputField = inputField
            )
            HomeExpandedTopBar(
                searchBarState = searchBarState,
                inputField = inputField
            )
        }
    }
}