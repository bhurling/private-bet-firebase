package io.bhurling.privatebet.feed

import com.google.firebase.database.DatabaseReference
import io.bhurling.privatebet.model.pojo.Bet
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.Observable

internal class GetKeysInteractor(
    private val firebase: ReactiveFirebase,
    private val feed: DatabaseReference
) {
    fun getKeys(): Observable<List<String>> {
        return firebase.observeValueEvents(this.feed.orderByValue())
            .map { it.children }
            .map { children ->
                children.mapNotNull { it.key }
            }
            .map { it.asReversed() }
    }
}