package io.bhurling.privatebet.feed

import io.bhurling.privatebet.arch.Presenter

internal class FeedPresenter(
    private val interactor: GetKeysInteractor
) : Presenter<FeedPresenter.View>() {

    override fun attachView(view: View) {
        super.attachView(view)

        disposables.addAll(
            interactor.getKeys()
                .subscribe {
                    view.updateKeys(it)
                }
        )
    }

    interface View : Presenter.View {
        fun updateKeys(keys: List<String>)
    }
}
