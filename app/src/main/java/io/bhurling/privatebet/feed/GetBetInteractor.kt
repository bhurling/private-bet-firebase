package io.bhurling.privatebet.feed

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import io.bhurling.privatebet.model.pojo.Bet
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.Observable

internal class GetBetInteractor(
    private val firebase: ReactiveFirebase,
    private val bets: CollectionReference
) {
    fun getBet(key: String): Observable<Bet> {
        return firebase
            .observeValueEvents(bets.document(key))
            .map { snapshot ->
                snapshot.toObject<Bet>()
            }
    }
}