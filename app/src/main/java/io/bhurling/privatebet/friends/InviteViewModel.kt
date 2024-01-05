package io.bhurling.privatebet.friends

import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bhurling.privatebet.arch.BaseViewModel
import io.bhurling.privatebet.arch.ViewModelEffect
import io.bhurling.privatebet.arch.ViewModelState
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import kotlinx.coroutines.rx2.asObservable
import javax.inject.Inject

@HiltViewModel
internal class InviteViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val invitationsInteractor: InvitationsInteractor,
    private val auth: FirebaseAuth
) : BaseViewModel<InviteAction, InviteState, ViewModelEffect>(InviteState()) {

    override fun onAttach() {
        disposables += Observables
                .combineLatest(
                        profileRepository.observeAll().asObservable(),
                        invitationsInteractor.incoming(),
                        invitationsInteractor.outgoing()
                )
                .map { (persons, incoming, outgoing) ->
                    persons.filter {
                        it.id != auth.currentUser?.uid
                    }.map {
                        InviteAdapterItem(
                                profile = it,
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