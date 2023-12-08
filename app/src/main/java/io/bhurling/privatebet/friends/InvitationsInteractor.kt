package io.bhurling.privatebet.friends

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import io.bhurling.privatebet.model.pojo.Person
import io.bhurling.privatebet.model.toPerson
import io.bhurling.privatebet.rx.firebase.ReactiveFirebase
import io.reactivex.Observable
import javax.inject.Inject

class InvitationsInteractor @Inject constructor(
    private val auth: FirebaseAuth,
    private val store: FirebaseFirestore,
    private val firebase: ReactiveFirebase,
) {

    private val myLinks by lazy {
        store.collection("links").document(auth.currentUser?.uid ?: "")
    }

    fun incoming(): Observable<List<Person>> = firebase
        .observeValueEvents(myLinks.collection("incoming"))
        .map { it.documents }
        .mapToPublicProfiles()
        .map { documents -> documents.mapSafely { it.toPerson() } }
        .distinctUntilChanged()

    fun outgoing(): Observable<List<Person>> = firebase
        .observeValueEvents(myLinks.collection("outgoing"))
        .map { it.documents }
        .mapToPublicProfiles()
        .map { documents -> documents.mapSafely { it.toPerson() } }
        .distinctUntilChanged()

    fun confirmed(): Observable<List<Person>> = firebase
        .observeValueEvents(myLinks.collection("confirmed"))
        .map { it.documents }
        .mapToPublicProfiles()
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

    private fun Observable<List<DocumentSnapshot>>.mapToPublicProfiles(): Observable<List<DocumentSnapshot>> {
        return switchMap { documents ->
            if (documents.isEmpty()) {
                Observable.just(emptyList())
            } else {
                firebase.observeValueEvents(
                    store.collection("public_profiles")
                        .whereIn(FieldPath.documentId(), documents.map { it.id })
                ).map { it.documents }
            }
        }
    }
}

private fun <T, R : Any> List<T>.mapSafely(mapper: (T) -> R): List<R> {
    return mapNotNull {
        try {
            mapper(it)
        } catch (e: Exception) {
            null
        }
    }
}

