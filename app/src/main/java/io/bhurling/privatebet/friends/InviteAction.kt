package io.bhurling.privatebet.friends

import io.bhurling.privatebet.arch.ViewModelAction

sealed class InviteAction : ViewModelAction {
    data class Invite(val id: String) : InviteAction()
    data class Revoke(val id: String) : InviteAction()
    data class Accept(val id: String) : InviteAction()
    data class Decline(val id: String) : InviteAction()
}