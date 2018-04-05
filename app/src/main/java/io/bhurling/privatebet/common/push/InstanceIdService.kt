package io.bhurling.privatebet.common.push

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import org.koin.inject

class InstanceIdService : FirebaseInstanceIdService() {

    private val auth: FirebaseAuth by inject()
    private val interactor: TokenInteractor by inject()

    override fun onTokenRefresh() {
        if (auth.currentUser == null) return

        FirebaseInstanceId.getInstance().token?.let { token ->
            interactor.addDeviceToken(token)
        }
    }
}