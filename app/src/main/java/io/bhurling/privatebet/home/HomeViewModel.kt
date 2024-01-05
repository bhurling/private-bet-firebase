package io.bhurling.privatebet.home

import dagger.hilt.android.lifecycle.HiltViewModel
import io.bhurling.privatebet.arch.BaseViewModel
import io.bhurling.privatebet.arch.ViewModelAction
import io.bhurling.privatebet.arch.ViewModelEffect
import io.bhurling.privatebet.arch.ViewModelState
import io.bhurling.privatebet.friends.InvitationsRepository
import kotlinx.coroutines.rx2.asObservable
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val invitationsRepository: InvitationsRepository
) : BaseViewModel<ViewModelAction, HomeState, ViewModelEffect>(HomeState()) {

    override fun onAttach() {
        disposables.addAll(
            invitationsRepository.confirmed().asObservable()
                .subscribe { confirmed ->
                    updateState { copy(isPrimaryActionVisible = confirmed.isNotEmpty()) }
                }
        )
    }
}

internal data class HomeState(
    val isPrimaryActionVisible: Boolean = false
) : ViewModelState
