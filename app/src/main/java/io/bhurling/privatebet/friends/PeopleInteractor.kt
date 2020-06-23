package io.bhurling.privatebet.friends

import com.google.firebase.firestore.CollectionReference
import io.bhurling.privatebet.model.pojo.Person
import io.bhurling.privatebet.model.toPerson
import io.bhurling.privatebet.rx.firebase.ReactiveFirebase
import io.reactivex.Observable

class PeopleInteractor(
    private val firebase: ReactiveFirebase,
    private val profiles: CollectionReference
) {

    fun all(): Observable<List<Person>> {
        return firebase
                .observeValueEvents(profiles.orderBy("displayName"))
                .map { snapshot ->
                    snapshot.documents.map { it.toPerson() }
                }
                .distinctUntilChanged()
    }

    fun byId(id: String): Observable<Person> {
        return firebase
            .observeValueEvents(profiles.document(id))
            .map { it.toPerson() }
    }
}
