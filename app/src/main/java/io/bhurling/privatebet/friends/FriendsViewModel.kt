package io.bhurling.privatebet.friends

import dagger.hilt.android.lifecycle.HiltViewModel
import io.bhurling.privatebet.arch.BaseViewModel
import io.bhurling.privatebet.arch.ViewModelEffect
import io.bhurling.privatebet.arch.ViewModelState
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

@HiltViewModel
internal class FriendsViewModel @Inject constructor(
        private val invitationsInteractor: InvitationsInteractor
) : BaseViewModel<InviteAction, FriendsState, ViewModelEffect>(FriendsState()) {

    override fun onAttach() {
        disposables +=
            Observables
                .combineLatest(
                    incoming(),
                    confirmed()
                ) { incoming, confirmed ->
                    incoming + confirmed
                }
                .map { invites ->
                    invites.reversed().distinctBy { it.profile.id }.reversed()
                }
                .subscribe { items ->
                    updateState { copy(items = items) }
                }
    }

    override fun handleActions(actions: Observable<InviteAction>) {
        disposables += actions
            .ofType<InviteAction.Accept>()
            .subscribe {
                invitationsInteractor.accept(it.id)
            }
    }

    private fun incoming(): Observable<List<FriendsAdapterItem>> {
        return invitationsInteractor.incoming()
                .map {
                    it.map {
                        FriendsAdapterItem(it, isInvited = true)
                    }
                }
    }

    private fun confirmed(): Observable<List<FriendsAdapterItem>> {
        return invitationsInteractor.confirmed()
                .map {
                    it.map {
                        FriendsAdapterItem(it, isConfirmed = true)
                    }
                }
    }
}

internal data class FriendsState(
    val items: List<FriendsAdapterItem>? = null
) : ViewModelState