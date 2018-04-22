package io.bhurling.privatebet.friends

import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import org.koin.inject

class AcceptInvitationService : IntentService("AcceptInvitationService") {

    private val interactor: InvitationsInteractor by inject()

    override fun onHandleIntent(intent: Intent) {
        interactor.accept(intent.getStringExtra(EXTRA_USER_ID))

        // TODO update notification action (we might need all the other content again)
    }

    companion object {
        private const val EXTRA_USER_ID = "io.bhurling.privatebet.extras.USER_ID"

        fun makePendingIntent(context: Context, userId: String): PendingIntent {
            val intent = Intent(context, AcceptInvitationService::class.java)
                    .putExtra(EXTRA_USER_ID, userId)

            return PendingIntent.getService(context, 0, intent, 0)
        }
    }
}