package io.bhurling.privatebet.friends

import com.google.firebase.firestore.FirebaseFirestore
import io.bhurling.privatebet.model.pojo.Person
import io.bhurling.privatebet.model.toPerson
import io.bhurling.privatebet.rx.firebase.ReactiveFirebase
import io.reactivex.Observable
import javax.inject.Inject

class PeopleInteractor @Inject constructor(
    private val firebase: ReactiveFirebase,
    private val store: FirebaseFirestore,
) {

    fun all(): Observable<List<Person>> {
        return firebase
            .observeValueEvents(store.profiles.orderBy("displayName"))
            .map { snapshot ->
                snapshot.documents.map { it.toPerson() }
            }
            .distinctUntilChanged()
    }

    fun byId(id: String): Observable<Person> {
        return firebase
            .observeValueEvents(store.profiles.document(id))
            .map { it.toPerson() }
    }
}

private val FirebaseFirestore.profiles get() = collection("public_profiles")