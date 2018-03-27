package io.bhurling.privatebet.friends

import com.google.firebase.database.DatabaseReference
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.Observable

class PeopleInteractor(
        private val firebase: ReactiveFirebase,
        private val profiles: DatabaseReference
) {

    fun all(): Observable<List<String>> {
        return firebase
                .observeValueEvents(profiles.orderByChild("displayName"))
                .take(1)
                .map { it.children.map { it.key } }
    }
}
