package io.bhurling.privatebet.common.push

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class TokenInteractor @Inject constructor(
    private val auth: FirebaseAuth,
    private val store: FirebaseFirestore
) {
    fun addDeviceToken(token: String) {
        auth.currentUser?.uid?.let { uid ->
            store.devices(uid).set(mapOf(token to true))
        }
    }
}

private fun FirebaseFirestore.devices(uid: String) = collection("devices").document(uid)
