package io.bhurling.privatebet.arch

import com.airbnb.mvrx.RealMvRxStateStore
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject

open class BaseViewModel<
        Action : ViewModelAction,
        State : ViewModelState,
        Effect : ViewModelEffect
        >(emptyState: State) {

    /**
     * Stream of effects the UI can subscribe to.
     */
    private val effects: PublishSubject<Effect> = PublishSubject.create<Effect>()

    /**
     * Container for the current state.
     */
    private val state = RealMvRxStateStore(emptyState)

    protected val disposables = CompositeDisposable()


    /**
     * This attaches a stream of actions to the ViewModel. Call this from a lifecycle method
     * in your view such as onCreate().
     *
     * Make sure to call detach when you're done with this.
     *
     * @param actions the stream of actions.
     */
    fun attach(actions: Observable<Action>? = null) {
        disposables.clear()

        onAttach()

        // Let the subclass subscribe to and handle the actions
        actions?.let { handleActions(it) }
    }

    /**
     * Subscribe to upstream events that are independent of view actions.
     *
     * Override this function in your own ViewModel.
     */
    protected open fun onAttach() {
        // Empty default implementation
    }

    /**
     * Call this when you're done with the ViewModel to release all internal resources.
     *
     * Call this from a lifecycle method in your view such as onDestroy().
     */
    fun detach() {
        disposables.clear()
    }

    /**
     * Subscribe to and handle view actions.
     *
     * Override this function in your own ViewModel.
     */
    protected open fun handleActions(actions: Observable<Action>) {
        // Empty default implementation
    }

    /**
     * Returns the stream of one-time effects.
     *
     * Emissions will always be on the main thread.
     */
    fun effects(): Observable<Effect> = effects.observeOn(AndroidSchedulers.mainThread())

    /**
     * Returns the stream of view states.
     *
     * Emissions will always be on the main thread.
     */
    fun states(): Observable<State> = state.observable.observeOn(AndroidSchedulers.mainThread())

    /**
     * Call this from inside your ViewModel to update the state.
     * The state reducer's receiver type is the current state when the reducer is called.
     *
     * An example of a reducer would be `{ copy(myProperty = 5) }`. The copy comes from the copy
     * function on a Kotlin data class and can be called directly because state is the receiver type
     * of the reducer. In this case, it will also implicitly return the only expression so that is
     * all of the code required.
     */
    protected fun updateState(stateReducer: State.() -> State) {
        state.set(stateReducer)
    }

    /**
     * Call this from inside your ViewModel to send an effect to the UI.
     */
    protected fun sendEffect(effect: Effect) {
        effects.onNext(effect)
    }

    /**
     * Convenience function to access the stream of changes to a single attribute of the view state.
     *
     * Emissions will always be on the main thread.
     *
     * @param extractor function to extract a single attribute of type [T] from the [State]
     */
    fun <T : Any> stateOf(extractor: State.() -> T): Observable<T> = state.observable
        .mapNotNull { extractor(it) }
        .distinctUntilChanged()
        .observeOn(AndroidSchedulers.mainThread())


}

/**
 * The actions that are consumed by the ViewModel.
 */
interface ViewModelAction

/**
 * The states that are emitted by the ViewModel.
 */
interface ViewModelState

/**
 * The one-time effects that may occur in the UI.
 * (Example: show a toast)
 */
interface ViewModelEffect