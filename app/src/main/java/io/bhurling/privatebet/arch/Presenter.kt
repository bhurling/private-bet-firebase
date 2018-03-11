package io.bhurling.privatebet.arch

import android.support.annotation.CallSuper

import io.reactivex.disposables.CompositeDisposable

abstract class Presenter<V : Presenter.View> {

    protected val disposables = CompositeDisposable()

    protected var view: V? = null

    @CallSuper
    open fun attachView(view: V) {
        this.view = view
    }

    @CallSuper
    fun detachView() {
        this.disposables.dispose()

        this.view = null
    }

    interface View
}
