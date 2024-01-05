package io.bhurling.privatebet.feed

import dagger.hilt.android.lifecycle.HiltViewModel
import io.bhurling.privatebet.arch.BaseViewModel
import io.bhurling.privatebet.arch.ViewModelAction
import io.bhurling.privatebet.arch.ViewModelEffect
import io.bhurling.privatebet.arch.ViewModelState
import kotlinx.coroutines.rx2.asObservable
import javax.inject.Inject

@HiltViewModel
internal class FeedViewModel @Inject constructor(
    private val repository: BetsRepository
) : BaseViewModel<ViewModelAction, FeedState, ViewModelEffect>(FeedState()) {

    override fun onAttach() {
        disposables.addAll(
            repository.keys().asObservable()
                .subscribe { keys ->
                    updateState { copy(keys = keys) }
                }
        )
    }
}

internal data class FeedState(
    val keys : List<String> = emptyList()
) : ViewModelState
