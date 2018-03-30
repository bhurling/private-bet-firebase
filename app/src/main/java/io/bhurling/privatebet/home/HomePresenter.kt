package io.bhurling.privatebet.home

import io.bhurling.privatebet.arch.Presenter
import io.bhurling.privatebet.friends.InvitationsInteractor

class HomePresenter(
        private val invitationsInteractor: InvitationsInteractor
) : Presenter<HomePresenter.View>() {

    override fun attachView(view: View) {
        super.attachView(view)

        disposables.addAll(
                invitationsInteractor.confirmed()
                        .subscribe {
                            when {
                                it.isNotEmpty() -> view.showPrimaryAction()
                                else -> view.hidePrimaryAction()
                            }
                        }
        )
    }

    interface View : Presenter.View {
        fun showPrimaryAction()
        fun hidePrimaryAction()
    }
}
