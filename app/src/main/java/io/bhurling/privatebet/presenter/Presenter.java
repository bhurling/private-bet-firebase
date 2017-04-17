package io.bhurling.privatebet.presenter;

import android.support.annotation.CallSuper;

import io.reactivex.disposables.CompositeDisposable;

public abstract class Presenter<V extends Presenter.View> {

    protected final CompositeDisposable disposables = new CompositeDisposable();

    protected V view;

    @CallSuper
    public void attachView(V view) {
        this.view = view;
    }

    @CallSuper
    public void detachView() {
        this.disposables.dispose();

        this.view = null;
    }

    public interface View {}
}
