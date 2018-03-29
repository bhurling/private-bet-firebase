package io.bhurling.privatebet.friends

sealed class InviteAction {
    data class Invite(val id: String) : InviteAction()
}