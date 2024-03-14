package io.hurling.privatebet.feature.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun FeedScreen() {
    val viewModel: FeedViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Feed(state = state)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Feed(state: FeedScreenState) {
    Column(modifier = Modifier.fillMaxSize()) {

        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.feed_screen_title)) },
        )
    }
}
