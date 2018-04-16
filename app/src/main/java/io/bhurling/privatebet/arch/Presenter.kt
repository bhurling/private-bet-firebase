package io.bhurling.privatebet.arch

import android.support.annotation.CallSuper

import io.reactivex.disposables.CompositeDisposable

abstract class Presenter<V : Presenter.View> {

    protected val disposables = CompositeDisposable()

    private var _view: V? = null

    protected val view: V
        get() {
            _view?.let { return it }
                    ?: error("Cannot access view before attachView() or after detachView()")
        }

    @CallSuper
    open fun attachView(view: V) {
        this._view = view
    }

    @CallSuper
    fun detachView() {
        this.disposables.dispose()

        this._view = null
    }

    interface View
}
