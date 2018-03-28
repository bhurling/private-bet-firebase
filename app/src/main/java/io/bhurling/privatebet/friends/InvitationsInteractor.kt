package io.bhurling.privatebet.friends

import com.google.firebase.database.DatabaseReference
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.Observable

class InvitationsInteractor(
        private val firebase: ReactiveFirebase,
        private val incoming: DatabaseReference,
        private val outgoing: DatabaseReference
) {

    fun incoming(): Observable<List<String>> = firebase.observeValueEvents(incoming)
            .map { it.children.map { it.key } }

    fun outgoing(): Observable<List<String>> = firebase.observeValueEvents(outgoing)
            .map { it.children.map { it.key } }

}