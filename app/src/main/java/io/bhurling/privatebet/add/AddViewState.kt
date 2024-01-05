package io.bhurling.privatebet.add

import io.bhurling.privatebet.arch.Optional
import io.bhurling.privatebet.arch.ViewModelState
import io.bhurling.privatebet.arch.none
import io.bhurling.privatebet.model.pojo.Profile

data class AddViewState(
    val step: Step = Step.STATEMENT,
    val statement: String = "",
    val deadline: Optional<Long> = none(),
    val stake: String = "",
    val opponentIds: List<String> = listOf(),
    val opponent: Profile? = null
) : ViewModelState {
    val shouldShowNextButton
        get() = step != Step.OPPONENT

    enum class Step {
        STATEMENT,
        STAKE,
        OPPONENT
    }
}