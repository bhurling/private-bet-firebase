package io.hurling.privatebet.feature.friends

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.hurling.privatebet.core.design.PrivateBetIcons
import io.hurling.privatebet.core.domain.InvitableProfile

@Composable
fun InviteScreen(onBackClick: () -> Unit) {
    val viewModel: InviteViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Invite(
        state = state,
        onRevokeInvitation = viewModel::revokeInvitation,
        onSendInvitation = viewModel::sendInvitation,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Invite(
    state: InviteScreenState,
    onRevokeInvitation: (String) -> Unit = {},
    onSendInvitation: (String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {

        CenterAlignedTopAppBar(
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = PrivateBetIcons.ArrowBack, contentDescription = null)
                }
            },
            title = { Text(text = stringResource(id = R.string.invite_screen_title)) },
        )

        when (state) {
            is InviteScreenState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is InviteScreenState.Success -> InviteList(
                items = state.items,
                onRevokeInvitation = onRevokeInvitation,
                onSendInvitation = onSendInvitation
            )
        }
    }
}

@Composable
private fun InviteList(
    items: List<InvitableProfile>,
    onRevokeInvitation: (String) -> Unit,
    onSendInvitation: (String) -> Unit
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
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    model = item.profile.photoUrl,
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    text = item.profile.displayName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                if (item.isInvited) {
                    FilledTonalButton(onClick = { onRevokeInvitation(item.profile.id) }) {
                        Text(text = stringResource(id = R.string.action_remove))
                    }
                } else {
                    FilledTonalButton(onClick = { onSendInvitation(item.profile.id) }) {
                        Text(text = stringResource(id = R.string.action_add))
                    }
                }
            }

        }
    }
}
