package io.bhurling.privatebet.common.push

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import io.bhurling.privatebet.common.notification.createChannels
import io.bhurling.privatebet.common.notification.invitation.InvitationReceivedNotificationService
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var interactor: TokenInteractor

    override fun onNewToken(token: String) {
        if (auth.currentUser == null) return

        interactor.addDeviceToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        createChannels(this)

        message.data.let {
            if (it.isNotEmpty()) {
                onDataMessageReceived(it)
            }
        }
    }

    private fun onDataMessageReceived(data: Map<String, String>) {
        when (data["key"]) {
            KEY_INVITATION_NEW -> handleNewInvitation(data)
        }
    }

    private fun handleNewInvitation(data: Map<String, String>) {
        val custom = JSONObject(data["custom"])

        val senderImage = custom.optString("senderImage")
        val senderId = custom.optString("senderId")
        val senderDisplayName = custom.optString("senderDisplayName")

        if (senderImage != null && senderId != null && senderDisplayName != null) {
            InvitationReceivedNotificationService.schedule(
                context = this,
                userId = senderId,
                photoUrl = senderImage,
                displayName = senderDisplayName
            )
        }
    }

    companion object {
        const val KEY_INVITATION_NEW = "InvitationNew"
    }
}