package io.bhurling.privatebet.common.push

import com.google.firebase.firestore.DocumentReference

class TokenInteractor(
        private val devices: DocumentReference
) {
    fun addDeviceToken(token: String) {
        devices.set(mapOf(token to true))
    }
}