package io.hurling.privatebet.feature.friends

import io.hurling.privatebet.core.domain.InvitableProfile

internal sealed interface InviteScreenState {
    data object Loading: InviteScreenState
    data class Success(val items: List<InvitableProfile>): InviteScreenState
}
