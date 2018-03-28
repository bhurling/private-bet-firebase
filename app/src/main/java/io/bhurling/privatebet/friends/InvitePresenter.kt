package io.bhurling.privatebet.friends

import io.bhurling.privatebet.arch.Presenter
import io.reactivex.rxkotlin.Observables

class InvitePresenter(
        private val peopleInteractor: PeopleInteractor,
        private val invitationsInteractor: InvitationsInteractor
) : Presenter<InvitePresenter.View>() {

    override fun attachView(view: View) {
        super.attachView(view)

        disposables.addAll(
                Observables
                        .combineLatest(
                                peopleInteractor.all(),
                                invitationsInteractor.incoming().startWith(listOf<String>()),
                                invitationsInteractor.outgoing().startWith(listOf<String>())
                        )
                        .map { (persons, incoming, outgoing) ->
                            persons.map {
                                InviteAdapterItem(
                                        person = it,
                                        isIncoming = incoming.contains(it.id),
                                        isSent = outgoing.contains(it.id)
                                )
                            }.filterNot { it.isIncoming && it.isSent }
                        }
                        .subscribe { view.updateItems(it) }
        )
    }

    interface View : Presenter.View {
        fun updateItems(items: List<InviteAdapterItem>)
    }
}