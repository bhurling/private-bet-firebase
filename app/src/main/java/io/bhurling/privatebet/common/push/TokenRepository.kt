package io.bhurling.privatebet.common.push

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class TokenRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val store: FirebaseFirestore
) {
    fun addDeviceToken(token: String) {
        auth.currentUser?.uid?.let { uid ->
            store.collection("devices").document(uid).set(mapOf(token to true))
        }
    }
}
