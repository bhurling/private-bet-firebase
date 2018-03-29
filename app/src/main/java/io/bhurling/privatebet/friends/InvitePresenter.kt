package io.bhurling.privatebet.friends

import io.bhurling.privatebet.arch.Presenter
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.plusAssign

class InvitePresenter(
        private val peopleInteractor: PeopleInteractor,
        private val invitationsInteractor: InvitationsInteractor
) : Presenter<InvitePresenter.View>() {

    override fun attachView(view: View) {
        super.attachView(view)

        disposables += Observables
                .combineLatest(
                        peopleInteractor.all(),
                        invitationsInteractor.incoming(),
                        invitationsInteractor.outgoing()
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

        disposables += view.actions()
                .ofType(InviteAction.Invite::class.java)
                .subscribe {
                    invitationsInteractor.invite(it.id)
                }

        disposables += view.actions()
                .ofType(InviteAction.Revoke::class.java)
                .subscribe {
                    invitationsInteractor.revoke(it.id)
                }

        disposables += view.actions()
                .ofType(InviteAction.Accept::class.java)
                .subscribe {
                    invitationsInteractor.accept(it.id)
                }

        disposables += view.actions()
                .ofType(InviteAction.Decline::class.java)
                .subscribe {
                    invitationsInteractor.decline(it.id)
                }
    }

    interface View : Presenter.View {
        fun actions(): Observable<InviteAction>
        fun updateItems(items: List<InviteAdapterItem>)
    }
}