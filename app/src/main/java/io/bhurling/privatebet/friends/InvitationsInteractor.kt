package io.bhurling.privatebet.friends

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.Observable

class InvitationsInteractor(
        private val firebase: ReactiveFirebase,
        private val links: DatabaseReference,
        private val me: FirebaseUser
) {

    fun incoming(): Observable<List<String>> = firebase
            .observeValueEvents(links.child(me.uid).child("incoming"))
            .map { it.children.map { it.key } }

    fun outgoing(): Observable<List<String>> = firebase
            .observeValueEvents(links.child(me.uid).child("outgoing"))
            .map { it.children.map { it.key } }

    fun invite(id: String) {
        links.child(id).child("incoming").child(me.uid).setValue(true)
        links.child(me.uid).child("outgoing").child(id).setValue(true)
    }

    fun revoke(id: String) {
        links.child(id).child("incoming").child(me.uid).removeValue()
        links.child(me.uid).child("outgoing").child(id).removeValue()
    }

    fun accept(id: String) {
        // TODO accept invitation
    }

    fun decline(id: String) {
        // TODO decline invitation
    }

}