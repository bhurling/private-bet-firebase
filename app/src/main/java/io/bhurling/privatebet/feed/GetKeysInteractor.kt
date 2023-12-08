package io.bhurling.privatebet.feed

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.bhurling.privatebet.rx.firebase.ReactiveFirebase
import io.reactivex.Observable
import javax.inject.Inject

internal class GetKeysInteractor @Inject constructor(
    private val firebase: ReactiveFirebase,
    private val store: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {

    private val feed by lazy {
        store.collection("feeds")
            .document(auth.currentUser?.uid ?: "")
            .collection("bets")
    }

    fun getKeys(): Observable<List<String>> {
        return firebase.observeValueEvents(feed)
            .map { it.documents }
            .map { documents ->
                documents.mapNotNull { it.id }
            }
            .map { it.asReversed() }
    }
}