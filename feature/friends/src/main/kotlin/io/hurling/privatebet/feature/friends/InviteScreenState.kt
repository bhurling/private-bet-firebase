package io.hurling.privatebet.feature.friends

internal sealed interface InviteScreenState {
    data object Loading: InviteScreenState
    data class Success(val items: List<InvitableProfile>): InviteScreenState
}
