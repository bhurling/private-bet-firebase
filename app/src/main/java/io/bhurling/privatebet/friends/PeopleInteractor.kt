package io.bhurling.privatebet.friends

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import io.bhurling.privatebet.model.pojo.Person
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.Observable

class PeopleInteractor(
        private val firebase: ReactiveFirebase,
        private val profiles: DatabaseReference
) {

    fun all(): Observable<List<Person>> {
        return firebase
                .observeValueEvents(profiles.orderByChild("displayName"))
                .take(1)
                .map { it.children.map { makePerson(it) } }
    }

    private fun makePerson(snapshot: DataSnapshot): Person {
        return Person(
                id = snapshot.key,
                displayName = snapshot.child("displayName").value?.toString() ?: "",
                photoUrl = snapshot.child("photoUrl").value?.toString() ?: ""
        )
    }
}
