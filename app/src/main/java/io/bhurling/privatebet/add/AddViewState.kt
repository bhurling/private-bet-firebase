package io.bhurling.privatebet.add

import io.bhurling.privatebet.model.pojo.Profile
import java.io.Serializable

data class AddViewState(
    val step: Step = Step.STATEMENT,
    val statement: String = "",
    val deadline: Long? = null,
    val stake: String = "",
    val contacts: List<Profile> = listOf(),
    val opponent: Profile? = null
) : Serializable {
    val shouldShowNextButton
        get() = step != Step.OPPONENT

    enum class Step {
        STATEMENT,
        STAKE,
        OPPONENT
    }

    sealed class StateUpdate {

        data object MoveForward : StateUpdate()
        data object MoveBack : StateUpdate()
        data class Deadline(val deadline: Long?) : StateUpdate()
        data class Stake(val stake: String) : StateUpdate()
        data class Statement(val statement: String) : StateUpdate()
        data class Opponent(val opponent: Profile) : StateUpdate()
        data class Contacts(val contacts: List<Profile>) : StateUpdate()
    }
}