package io.bhurling.privatebet.friends

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import io.bhurling.privatebet.model.pojo.Person
import io.bhurling.privatebet.model.toPerson
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.Observable

class InvitationsInteractor(
    private val firebase: ReactiveFirebase,
    private val links: CollectionReference,
    private val me: FirebaseUser
) {

    private val myLinks = links.document(me.uid)

    fun incoming(): Observable<List<Person>> = firebase
        .observeValueEvents(myLinks.collection("incoming"))
        .map { it.documents }
        .map { documents -> documents.mapSafely { it.toPerson() } }
        .distinctUntilChanged()

    fun outgoing(): Observable<List<Person>> = firebase
        .observeValueEvents(myLinks.collection("outgoing"))
        .map { it.documents }
        .map { documents -> documents.mapSafely { it.toPerson() } }
        .distinctUntilChanged()

    fun confirmed(): Observable<List<Person>> = firebase
        .observeValueEvents(myLinks.collection("confirmed"))
        .map { it.documents }
        .map { documents -> documents.mapSafely { it.toPerson() } }
        .distinctUntilChanged()

    fun invite(id: String) {
        myLinks.collection("outgoing").document(id).set(mapOf("linked" to true))
    }

    fun revoke(id: String) {
        myLinks.collection("outgoing").document(id).delete()
    }

    fun accept(id: String) {
        myLinks.collection("confirmed").document(id).set(mapOf("linked" to true))
    }

    fun decline(id: String) {
        myLinks.collection("incoming").document(id).delete()
    }

}

private fun <T, R : Any> List<T>.mapSafely(mapper: (T) -> R) : List<R> {
    return mapNotNull {
        try {
            mapper(it)
        } catch (e: Exception) {
            null
        }
    }
}

