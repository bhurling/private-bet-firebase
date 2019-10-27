package io.bhurling.privatebet.friends

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import io.bhurling.privatebet.model.pojo.Person
import io.bhurling.privatebet.model.toPerson
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.Observable

class PeopleInteractor(
        private val firebase: ReactiveFirebase,
        private val profiles: CollectionReference,
        private val me: FirebaseUser
) {

    fun all(): Observable<List<Person>> {
        return firebase
                .observeValueEvents(profiles.orderBy("displayName"))
                .map { snapshot ->
                    snapshot.documents
                        .filter { child -> child.id != me.uid }
                        .map { it.toPerson() }
                }
    }

    fun byId(id: String): Observable<Person> {
        return firebase
            .observeValueEvents(profiles.document(id))
            .map { it.toPerson() }
    }
}
