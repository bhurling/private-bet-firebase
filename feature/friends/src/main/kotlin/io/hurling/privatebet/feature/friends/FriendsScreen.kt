package io.hurling.privatebet.feature.friends

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.hurling.privatebet.core.design.PrivateBetIcons
import io.hurling.privatebet.core.design.Theme
import io.hurling.privatebet.core.domain.Friend

@Composable
fun FriendsScreen(onConnectClick: () -> Unit) {
    val viewModel: FriendsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Friends(
        state = state,
        onAcceptInvitation = viewModel::acceptInvitation,
        onConnectClick = onConnectClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Friends(
    state: FriendsScreenState,
    onAcceptInvitation: (String) -> Unit = {},
    onConnectClick: () -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize()) {

        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.friends_screen_title)) },
            actions = {
                IconButton(onClick = onConnectClick) {
                    Icon(imageVector = PrivateBetIcons.AddPerson, contentDescription = null)
                }
            }
        )

        when (state) {
            is FriendsScreenState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is FriendsScreenState.Success -> when {
                state.items.isEmpty() -> FriendsEmptyState(onConnectClick = onConnectClick)
                else -> FriendsList(items = state.items, onAcceptInvitation = onAcceptInvitation)
            }
        }
    }
}

@Composable
fun FriendsList(
    items: List<Friend>,
    onAcceptInvitation: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(72.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF777777)),
                    model = item.photoUrl,
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    text = item.displayName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                if (item.isIncoming && !item.isConfirmed) {
                    OutlinedButton(
                        onClick = { onAcceptInvitation(item.id) },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.secondary
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(text = stringResource(id = R.string.action_accept))
                    }
                }
            }
        }
    }

}

@Composable
fun FriendsEmptyState(
    onConnectClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.friends_empty_title),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineSmall
        )
        Box(
            modifier = Modifier
                .padding(32.dp)
                .size(120.dp)
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(80.dp),
                imageVector = PrivateBetIcons.Group,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            modifier = Modifier.widthIn(max = 240.dp),
            text = stringResource(id = R.string.friends_empty_body),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedButton(
            onClick = onConnectClick,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = stringResource(id = R.string.friends_empty_button_label))
        }
    }
}

@Preview
@Composable
internal fun FriendsEmptyStatePreview() {
    Theme {
        FriendsEmptyState(
            onConnectClick = {}
        )
    }
}
