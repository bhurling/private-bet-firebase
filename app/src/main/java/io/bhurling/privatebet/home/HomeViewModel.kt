package io.bhurling.privatebet.home

import io.bhurling.privatebet.arch.BaseViewModel
import io.bhurling.privatebet.arch.ViewModelAction
import io.bhurling.privatebet.arch.ViewModelEffect
import io.bhurling.privatebet.arch.ViewModelState
import io.bhurling.privatebet.friends.InvitationsInteractor

internal class HomeViewModel(
        private val invitationsInteractor: InvitationsInteractor
) : BaseViewModel<ViewModelAction, HomeState, ViewModelEffect>(HomeState()) {

    override fun onAttach() {
        disposables.addAll(
            invitationsInteractor.confirmed()
                .subscribe { confirmed ->
                    updateState { copy(isPrimaryActionVisible = confirmed.isNotEmpty()) }
                }
        )
    }
}

internal data class HomeState(
    val isPrimaryActionVisible: Boolean = false
) : ViewModelState
