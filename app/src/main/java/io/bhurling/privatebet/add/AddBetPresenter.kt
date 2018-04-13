package io.bhurling.privatebet.add

import io.bhurling.privatebet.arch.Presenter
import io.bhurling.privatebet.common.Optional
import io.bhurling.privatebet.common.get
import io.bhurling.privatebet.common.isSome
import io.bhurling.privatebet.common.none
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject

class AddBetPresenter constructor(

) : Presenter<AddBetPresenter.View>() {

    private val viewStateSubject = PublishSubject.create<ViewState>()

    override fun attachView(view: View) {
        super.attachView(view)

        disposables += viewStateSubject
                .switchMap { state ->
                    view.deadlineChanges()
                            .map { state.copy(deadline = it) }
                }
                .subscribe { viewStateSubject.onNext(it) }

        disposables += viewStateSubject
                .switchMap { state ->
                    view.clearDeadlineClicks()
                            .map { state.copy(deadline = none()) }
                }
                .subscribe { viewStateSubject.onNext(it) }

        disposables += viewStateSubject
                .map { it.deadline }
                .distinctUntilChanged()
                .switchMap { deadline -> view.deadlineClicks().map { deadline } }
                .subscribe { view.showDeadlinePicker(it) }

        disposables += viewStateSubject
                .map { it.deadline }
                .distinctUntilChanged()
                .subscribe {
                    if (it.isSome) {
                        view.setDeadline(it.get())
                    } else {
                        view.removeDeadline()
                    }
                }

        viewStateSubject.onNext(ViewState())
    }

    data class ViewState(
            val statement: String = "",
            val deadline: Optional<Long> = none()
    )

    interface View : Presenter.View {
        fun deadlineClicks(): Observable<Unit>
        fun deadlineChanges(): Observable<Optional<Long>>
        fun showDeadlinePicker(currentDeadline: Optional<Long>)
        fun setDeadline(deadline: Long)
        fun removeDeadline()
        fun clearDeadlineClicks(): Observable<Unit>
    }
}
