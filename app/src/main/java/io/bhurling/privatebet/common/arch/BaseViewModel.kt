package io.bhurling.privatebet.common.arch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.io.Serializable

private const val KEY_UI_STATE = "savedStateKeyUiState"

/**
 * Base class for all view models used in this project. The main concepts to understand are:
 *
 * - [State] - Full description of the current ui state. This is what is exposed as a flow
 * via the [state] property. Needs to be [Serializable] so we can save it in in the saved state.
 *
 * - [StateUpdate] - Partial update to the ui state. This is usually a sealed class with the different
 * kinds of updates that can happen to the ui state. E.g. the result of a fetch operation or the fact
 * that this operation failed.
 *
 * - [Effect] - A one-shot event that needs to be handled by the UI such as closing the screen,
 * showing a toast or navigating to a details page. Effects get exposed via the [effects] flow.
 *
 * - [Action] - Actions that can be performed from the UI side and that should trigger state updates.
 * Typical actions are button clicks or changes to a text input field.
 *
 * The flow of data can be described as follows:
 * 1. The UI calls [offer] with a new [Action] (e.g. after the user clicked on a button)
 * 2. The incoming action is [execute]d and turned into a flow of [StateUpdate]s
 * (this could be an update to show a loading state followed by the result)
 * 3. Each [StateUpdate] then is [reduce]d to a new full [State]
 *
 * Example:
 *
 * ```
 * // 'execute' turns an incoming action into a flow of state updates
 * override fun execute(action: Action) = when (action) {
 *     is Action.LoadData -> flow {
 *         emit(StateUpdate.Loading)
 *
 *         runCatching {
 *             loadData()
 *         }.onSuccess { result ->
 *             emit(StateUpdate.Result(result))
 *         }.onFailure {
 *             emit(StateUpdate.Failure)
 *         }
 *     }
 * }
 *
 * // 'reduce' combines state updates with the current state
 * override fun reduce(state: State, update: StateUpdate) = when (update) {
 *     is StateUpdate.Loading -> {
 *         state.copy(isLoading = true)
 *     }
 *     is StateUpdate.Result -> {
 *         state.copy(isLoading = false, result = update.result)
 *     }
 *     is StateUpdate.Error -> {
 *         state.copy(isError = true, isLoading = false)
 *     }
 * }
 * ```
 */
abstract class BaseViewModel<State : Serializable, StateUpdate, Effect, Action>(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val actions = MutableSharedFlow<Action>()

    /**
     * Exposes the current state of the UI. Typically, you will call `collectAsState()` in your composable.
     */
    val state by lazy { savedStateHandle.getStateFlow(KEY_UI_STATE, initialState()) }

    private val _effects = Channel<Effect>()

    /**
     * Exposes one-shot events to the UI. Effects will only ever be sent once. New effects are suspended until
     * the previous effect was delivered. Typically, you will observe effects like so:
     *
     * ```
     * @Composable
     * fun MyScreen(onEffect: (Effect) -> Unit) {
     *     val viewModel: MyViewModel = viewModel()
     *     val effect by viewModel.effects.collectAsState(initial = null)
     *
     *     EffectConsumer(effect = effect, onEffect = onEffect)
     * }
     * ```
     *
     * The `onEffect` argument can be passed in from a fragment or activity in order to delegate effect handling.
     */
    val effects = _effects.receiveAsFlow()

    init {
        viewModelScope.launch {
            actions
                .map { execute(it) }
                .flattenMerge()
                .scan(state.value, ::reduce)
                .collect {
                    savedStateHandle[KEY_UI_STATE] = it
                }
        }
    }

    /**
     * Post a UI action such as a button click or input changes. Actions can lead to an updated
     * state of the UI. See [execute] and [reduce] for further details.
     *
     * @param action the action from the UI that might or might not update the UI state.
     *
     * @see execute
     * @see reduce
     */
    fun offer(action: Action) {
        viewModelScope.launch {
            actions.emit(action)
        }
    }

    /**
     * Send an effect to the UI. Sending will be suspended if another effect is in flight.
     *
     * @param effect the effect to be sent.
     */
    fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }

    protected suspend fun withState(handler: suspend (State) -> Unit) {
        state.take(1).collect(handler)
    }

    /**
     * Provides the initial state for this screen.
     */
    protected abstract fun initialState(): State

    /**
     * Turns incoming UI [Action]s into a flow of [StateUpdate]s. In some cases this can be as simple
     * as taking in the action's parameters and returning a flow with a single state update (using `flowOf()`).
     *
     * In other cases an incoming action might trigger a whole sequence of state updates
     * (e.g. a loading update followed by the result of some network operation).
     *
     * If an incoming action does not need to update the UI, you can return `emptyFlow()`.
     *
     * @param action the incoming UI action that will trigger state updates.
     * @return flow of state updates.
     */
    protected abstract fun execute(action: Action): Flow<StateUpdate>

    /**
     * Merges a [StateUpdate] with the current [State]. This usually takes arguments inside of the
     * [StateUpdate] and copies them over to the [State] object (data classes are handy).
     *
     * @param state describes the current state of the UI. Use this argument to create your own copy of the updated state.
     * @param update describes the update that should be applied to the state.
     * @return the updated state
     */
    protected abstract fun reduce(state: State, update: StateUpdate) : State
}
