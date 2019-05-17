package io.bhurling.privatebet.friends

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.Observable

class InvitationsInteractor(
    private val firebase: ReactiveFirebase,
    private val links: CollectionReference,
    private val me: FirebaseUser
) {

    private val myLinks = links.document(me.uid)

    fun incoming(): Observable<List<String>> = firebase
        .observeValueEvents(myLinks.collection("incoming"))
        .mapToChildKeys()

    fun outgoing(): Observable<List<String>> = firebase
        .observeValueEvents(myLinks.collection("outgoing"))
        .mapToChildKeys()

    fun confirmed(): Observable<List<String>> = firebase
        .observeValueEvents(myLinks.collection("confirmed"))
        .mapToChildKeys()

    fun invite(id: String) {
        links.document(id).collection("incoming").document(me.uid).set(mapOf("linked" to true))
        myLinks.collection("outgoing").document(id).set(mapOf("linked" to true))
    }

    fun revoke(id: String) {
        links.document(id).collection("incoming").document(me.uid).delete()
        myLinks.collection("outgoing").document(id).delete()
    }

    fun accept(id: String) {
        links.document(id).collection("confirmed").document(me.uid).set(mapOf("linked" to true))
        myLinks.collection("confirmed").document(id).set(mapOf("linked" to true))
        links.document(id).collection("incoming").document(me.uid).set(mapOf("linked" to true))
        myLinks.collection("outgoing").document(id).set(mapOf("linked" to true))
    }

    fun decline(id: String) {
        // TODO decline invitation
    }

}

private fun Observable<QuerySnapshot>.mapToChildKeys() =
    map { snapshots ->
        snapshots.documents.mapNotNull { snapshot -> snapshot.id }
    }
