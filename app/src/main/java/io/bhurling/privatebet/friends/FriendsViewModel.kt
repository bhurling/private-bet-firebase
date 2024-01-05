package io.bhurling.privatebet.friends

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
internal class FriendsViewModel @Inject constructor(
    private val invitationsRepository: InvitationsRepository
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
                invitationsRepository.accept(it.id)
            }
    }

    private fun incoming(): Observable<List<FriendsAdapterItem>> {
        return invitationsRepository.incoming().asObservable()
            .map { incoming ->
                incoming.map { profile ->
                    FriendsAdapterItem(profile, isInvited = true)
                }
            }
    }

    private fun confirmed(): Observable<List<FriendsAdapterItem>> {
        return invitationsRepository.confirmed().asObservable()
            .map { confirmed ->
                confirmed.map { profile ->
                    FriendsAdapterItem(profile, isConfirmed = true)
                }
            }
    }
}

internal data class FriendsState(
    val items: List<FriendsAdapterItem>? = null
) : ViewModelState