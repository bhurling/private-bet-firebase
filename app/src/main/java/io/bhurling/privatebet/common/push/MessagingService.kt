package io.bhurling.privatebet.common.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.bhurling.privatebet.common.job.InvitationReceivedNotificationService
import io.bhurling.privatebet.common.notification.createChannels
import org.json.JSONObject

class MessagingService : FirebaseMessagingService() {

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
        val id = data["id"]
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