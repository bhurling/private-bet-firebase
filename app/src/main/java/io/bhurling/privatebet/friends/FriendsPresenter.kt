package io.bhurling.privatebet.friends

import io.bhurling.privatebet.arch.Presenter

class FriendsPresenter constructor(
        private val invitationsInteractor: InvitationsInteractor
) : Presenter<FriendsPresenter.View>() {

    override fun attachView(view: View) {
        super.attachView(view)

        disposables.addAll(
                invitationsInteractor.confirmed()
                        .subscribe {
                            when {
                                it.isEmpty() -> view.showEmptyState()
                                else -> view.hideEmptyState()
                            }
                        }
        )
    }

    interface View : Presenter.View {
        fun showEmptyState()
        fun hideEmptyState()
    }
}
