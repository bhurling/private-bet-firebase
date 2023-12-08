package io.bhurling.privatebet.feed

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import io.bhurling.privatebet.model.pojo.Bet
import io.bhurling.privatebet.rx.firebase.ReactiveFirebase
import io.reactivex.Observable
import javax.inject.Inject

internal class GetBetInteractor @Inject constructor(
    private val firebase: ReactiveFirebase,
    private val store: FirebaseFirestore,
) {

    private val bets by lazy {
        store.collection("bets")
    }

    fun getBet(key: String): Observable<Bet> {
        return firebase
            .observeValueEvents(bets.document(key))
            .map { snapshot ->
                snapshot.toObject<Bet>()
            }
    }
}
