package io.bhurling.privatebet.friends

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import io.bhurling.privatebet.R
import io.bhurling.privatebet.common.ui.Theme

@AndroidEntryPoint
internal class InviteActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(ComposeView(this).apply {
            setContent {
                InviteScreen(
                    onClose = this@InviteActivity::finish
                )
            }
        })
    }
}

@Composable
internal fun InviteScreen(
    onClose: () -> Unit
) {
    val viewModel: InviteViewModel = viewModel()
    val state by viewModel.state.collectAsState(initial = InviteState())

    Invite(
        state = state,
        onSendInvite = viewModel::sendInvite,
        onRevokeInvite = viewModel::revokeInvite,
        onClose = onClose
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Invite(
    state: InviteState,
    onRevokeInvite: (String) -> Unit = {},
    onSendInvite: (String) -> Unit = {},
    onClose: () -> Unit = {}
) {
    Theme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Invite friends", style = MaterialTheme.typography.titleMedium)
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    navigationIcon = {
                        IconButton(onClick = onClose) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }
        ) { padding ->
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(state.items) { item ->
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

                        if (item.isSent) {
                            OutlinedButton(
                                onClick = { onRevokeInvite(item.profile.id) },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.secondary
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Text(text = stringResource(id = R.string.action_remove))
                            }
                        } else {
                            OutlinedButton(
                                onClick = { onSendInvite(item.profile.id) },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.secondary
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Text(text = stringResource(id = R.string.action_add))
                            }
                        }
                    }

                }
            }

        }
    }
}
