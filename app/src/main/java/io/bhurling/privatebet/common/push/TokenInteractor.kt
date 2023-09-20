package io.bhurling.privatebet.common.push

import com.google.firebase.firestore.DocumentReference
import io.bhurling.privatebet.DeviceDocument
import javax.inject.Inject

class TokenInteractor @Inject constructor(
    @DeviceDocument private val devices: DocumentReference
) {
    fun addDeviceToken(token: String) {
        devices.set(mapOf(token to true))
    }
}
