package io.hurling.privatebet.feature.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.hurling.privatebet.core.design.PreviewScaffold
import io.hurling.privatebet.core.design.PrivateBetIcons

@Composable
internal fun FeedScreen(onCreateBetClick: () -> Unit) {
    val viewModel: FeedViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Feed(state = state, onCreateBetClick = onCreateBetClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Feed(
    state: FeedScreenState,
    onCreateBetClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.feed_screen_title)) },
        )

        Box(modifier = Modifier.fillMaxSize()) {
            FloatingActionButton(
                modifier = Modifier
                    .padding(24.dp)
                    .align(Alignment.BottomEnd),
                onClick = onCreateBetClick
            ) {
                Icon(imageVector = PrivateBetIcons.Plus, contentDescription = null)
            }
        }
    }
}

@Preview
@Composable
private fun FeedPreview() {
    PreviewScaffold {
        Feed(state = object : FeedScreenState {}, onCreateBetClick = {})
    }
}
