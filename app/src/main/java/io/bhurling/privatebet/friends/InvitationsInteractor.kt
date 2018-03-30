package io.bhurling.privatebet.friends

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.Observable

class InvitationsInteractor(
        private val firebase: ReactiveFirebase,
        private val links: DatabaseReference,
        private val me: FirebaseUser
) {

    private val myLinks = links.child(me.uid)

    fun incoming(): Observable<List<String>> = firebase
            .observeValueEvents(myLinks.child("incoming"))
            .mapToChildKeys()

    fun outgoing(): Observable<List<String>> = firebase
            .observeValueEvents(myLinks.child("outgoing"))
            .mapToChildKeys()

    fun confirmed(): Observable<List<String>> = firebase
            .observeValueEvents(myLinks.child("confirmed"))
            .mapToChildKeys()

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

private fun Observable<DataSnapshot>.mapToChildKeys() = map { it.children.map { it.key } }
