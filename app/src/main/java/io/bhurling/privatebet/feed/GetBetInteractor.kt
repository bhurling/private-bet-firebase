package io.bhurling.privatebet.feed

import com.google.firebase.database.DatabaseReference
import io.bhurling.privatebet.model.pojo.Bet
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.Observable

internal class GetBetInteractor(
    private val firebase: ReactiveFirebase,
    private val bets: DatabaseReference
) {
    fun getBet(key: String): Observable<Bet> {
        return firebase
            .observeValueEvents(bets.child(key))
            .map { dataSnapshot ->
                dataSnapshot.getValue(Bet::class.java)
            }
    }
}