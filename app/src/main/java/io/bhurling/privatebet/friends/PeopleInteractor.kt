package io.bhurling.privatebet.friends

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import io.bhurling.privatebet.model.pojo.Person
import io.bhurling.privatebet.model.toPerson
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.Observable

class PeopleInteractor(
        private val firebase: ReactiveFirebase,
        private val profiles: DatabaseReference,
        private val me: FirebaseUser
) {

    fun all(): Observable<List<Person>> {
        return firebase
                .observeValueEvents(profiles.orderByChild("displayName"))
                .take(1)
                .map { snapshot ->
                    snapshot.children
                        .filter { child -> child.key != me.uid }
                        .map { it.toPerson() }
                }
    }

    fun byId(id: String): Observable<Person> {
        return firebase
            .observeValueEvents(profiles.child(id))
            .map { it.toPerson() }
    }
}
