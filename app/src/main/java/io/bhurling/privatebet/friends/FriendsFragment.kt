package io.bhurling.privatebet.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import io.bhurling.privatebet.R
import io.bhurling.privatebet.common.ui.Theme
import io.bhurling.privatebet.model.pojo.Profile
import io.bhurling.privatebet.navigation.EntryPoint
import io.bhurling.privatebet.navigation.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class FriendsFragment : Fragment() {

    @Inject
    lateinit var adapter: FriendsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {
                FriendsScreen(
                    onShouldShowMenuChange = { setHasOptionsMenu(it) },
                    onConnectClick = {
                        EntryPoint.Invite.launch(requireContext())
                    }
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.friends, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_invite) {
            EntryPoint.Invite.launch(requireContext())

            return true
        }

        return super.onOptionsItemSelected(item)
    }
}

@Composable
internal fun FriendsScreen(
    onShouldShowMenuChange: (Boolean) -> Unit = {},
    onConnectClick: () -> Unit = {},
) {
    val viewModel: FriendsViewModel = viewModel()
    val state by viewModel.state.collectAsState(initial = FriendsState())

    LaunchedEffect(state.shouldShowMenu) {
        onShouldShowMenuChange(state.shouldShowMenu)
    }

    Friends(
        state = state,
        onAcceptInvitation = { id -> viewModel.acceptInvitation(id) },
        onConnectClick = onConnectClick
    )
}

@Composable
internal fun Friends(
    state: FriendsState,
    onAcceptInvitation: (String) -> Unit = {},
    onConnectClick: () -> Unit = {},
) {
    Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.items.isEmpty()) {
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
                    Image(
                        modifier = Modifier
                            .padding(vertical = 32.dp)
                            .size(120.dp)
                            .background(
                                color = MaterialTheme.colorScheme.onPrimary,
                                shape = CircleShape
                            ),
                        painter = painterResource(id = R.drawable.ic_people_black_56dp),
                        contentDescription = null,
                        contentScale = ContentScale.Inside,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )
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
            } else {
                LazyColumn {
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

                            if (item.isIncoming && !item.isConfirmed) {
                                OutlinedButton(
                                    onClick = { onAcceptInvitation(item.profile.id) },
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
        }
    }
}

@Preview
@Composable
fun FriendsPreviewWithItem() {
    Friends(
        state = FriendsState(
            items = listOf(
                FriendsItem(
                    profile = Profile(
                        id = "1",
                        displayName = "Veronica Martinez",
                        photoUrl = null
                    ),
                    isConfirmed = false,
                    isIncoming = true
                )
            )
        )
    )
}

@Preview
@Composable
fun FriendsPreviewEmpty() {
    Friends(
        state = FriendsState(
            items = emptyList()
        )
    )
}
