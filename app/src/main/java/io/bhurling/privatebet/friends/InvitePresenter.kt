package io.bhurling.privatebet.friends

import io.bhurling.privatebet.arch.Presenter

class InvitePresenter(
        private val peopleInteractor: PeopleInteractor
) : Presenter<InvitePresenter.View>() {

    override fun attachView(view: View) {
        super.attachView(view)

        disposables.addAll(

        )
    }

    interface View : Presenter.View
}