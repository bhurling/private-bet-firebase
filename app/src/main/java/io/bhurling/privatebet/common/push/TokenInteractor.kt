package io.bhurling.privatebet.common.push

import com.google.firebase.database.DatabaseReference

class TokenInteractor(
        private val devices: DatabaseReference
) {
    fun addDeviceToken(token: String) {
        devices.child(token).setValue(true)
    }
}