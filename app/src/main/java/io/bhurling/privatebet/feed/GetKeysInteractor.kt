package io.bhurling.privatebet.feed

import com.google.firebase.firestore.CollectionReference
import io.bhurling.privatebet.rx.firebase.ReactiveFirebase
import io.reactivex.Observable

internal class GetKeysInteractor(
    private val firebase: ReactiveFirebase,
    private val feed: CollectionReference
) {
    fun getKeys(): Observable<List<String>> {
        return firebase.observeValueEvents(this.feed)
            .map { it.documents }
            .map { documents ->
                documents.mapNotNull { it.id }
            }
            .map { it.asReversed() }
    }
}