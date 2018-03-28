package io.bhurling.privatebet.friends

import io.bhurling.privatebet.arch.Presenter

class InvitePresenter(
        private val peopleInteractor: PeopleInteractor
) : Presenter<InvitePresenter.View>() {

    override fun attachView(view: View) {
        super.attachView(view)

        disposables.addAll(
                peopleInteractor.all()
                        .map { it.map { InviteAdapterItem(it, false, false) } }
                        .subscribe { view.updateItems(it) }
        )
    }

    interface View : Presenter.View {
        fun updateItems(items: List<InviteAdapterItem>)
    }
}