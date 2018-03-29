package io.bhurling.privatebet.friends

sealed class InviteAction {
    data class Invite(val id: String) : InviteAction()
    data class Revoke(val id: String) : InviteAction()
    data class Accept(val id: String) : InviteAction()
    data class Decline(val id: String) : InviteAction()
}