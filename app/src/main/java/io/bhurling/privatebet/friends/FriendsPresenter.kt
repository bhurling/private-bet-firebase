package io.bhurling.privatebet.friends

import io.bhurling.privatebet.arch.Presenter
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.plusAssign

class FriendsPresenter constructor(
        private val invitationsInteractor: InvitationsInteractor
) : Presenter<FriendsPresenter.View>() {

    override fun attachView(view: View) {
        super.attachView(view)

        disposables +=
                Observables
                        .combineLatest(
                                incoming(),
                                confirmed(),
                                { incoming, confirmed -> incoming + confirmed }
                        )
                        .map { it.reversed().distinctBy { it.id }.reversed() }
                        .subscribe {
                            when {
                                it.isNotEmpty() -> view.showContent(it)
                                else -> view.showEmptyState()
                            }
                        }

        disposables += view.actions()
                .ofType(InviteAction.Accept::class.java)
                .subscribe {
                    invitationsInteractor.accept(it.id)
                }
    }

    private fun incoming(): Observable<List<FriendsAdapterItem>> {
        return invitationsInteractor.incoming()
                .map {
                    it.map {
                        FriendsAdapterItem(it, isInvited = true)
                    }
                }
    }

    private fun confirmed(): Observable<List<FriendsAdapterItem>> {
        return invitationsInteractor.confirmed()
                .map {
                    it.map {
                        FriendsAdapterItem(it, isConfirmed = true)
                    }
                }
    }

    interface View : Presenter.View {
        fun actions(): Observable<InviteAction>
        fun showEmptyState()
        fun showContent(items: List<FriendsAdapterItem>)
    }
}
