package io.bhurling.privatebet.friends

import com.google.firebase.auth.FirebaseUser
import io.bhurling.privatebet.arch.BaseViewModel
import io.bhurling.privatebet.arch.ViewModelEffect
import io.bhurling.privatebet.arch.ViewModelState
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign

internal class InviteViewModel(
        private val peopleInteractor: PeopleInteractor,
        private val invitationsInteractor: InvitationsInteractor,
        private val me: FirebaseUser
) : BaseViewModel<InviteAction, InviteState, ViewModelEffect>(InviteState()) {

    override fun onAttach() {
        disposables += Observables
                .combineLatest(
                        peopleInteractor.all(),
                        invitationsInteractor.incoming(),
                        invitationsInteractor.outgoing()
                )
                .map { (persons, incoming, outgoing) ->
                    persons.filter {
                        it.id != me.uid
                    }.map {
                        InviteAdapterItem(
                                person = it,
                                isIncoming = incoming.contains(it),
                                isSent = outgoing.contains(it)
                        )
                    }.filterNot { it.isIncoming }
                }
                .subscribe { items ->
                    updateState { copy(items = items) }
                }
    }

    override fun handleActions(actions: Observable<InviteAction>) {
        disposables += actions
                .ofType<InviteAction.Invite>()
                .subscribe {
                    invitationsInteractor.invite(it.id)
                }

        disposables += actions
                .ofType<InviteAction.Revoke>()
                .subscribe {
                    invitationsInteractor.revoke(it.id)
                }

        disposables += actions
                .ofType<InviteAction.Accept>()
                .subscribe {
                    invitationsInteractor.accept(it.id)
                }

        disposables += actions
                .ofType<InviteAction.Decline>()
                .subscribe {
                    invitationsInteractor.decline(it.id)
                }
    }
}

internal data class InviteState(
        val items: List<InviteAdapterItem> = emptyList()
) : ViewModelState