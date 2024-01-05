package io.bhurling.privatebet.feed

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
internal class FeedViewModel @Inject constructor(
    private val repository: BetsRepository
) : ViewModel() {

    val state = repository.keys()
        .map { keys ->
            FeedState(keys = keys)
        }
}

internal data class FeedState(
    val keys: List<String> = emptyList()
)
