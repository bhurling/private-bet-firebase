package io.bhurling.privatebet

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import io.bhurling.privatebet.add.AddBetActivity
import io.bhurling.privatebet.friends.AcceptInvitationService
import io.bhurling.privatebet.friends.InviteActivity
import io.bhurling.privatebet.home.HomeActivity
import io.bhurling.privatebet.signup.SignupActivity
import io.bhurling.privatebet.signup.SignupService

class Navigator {

    fun launchSignupFlow(activity: Activity?) {
        activity?.startActivity(Intent(activity, SignupActivity::class.java))
    }

    fun launchApp(activity: Activity?) {
        activity?.startService(Intent(activity, SignupService::class.java))
        activity?.startActivity(Intent(activity, HomeActivity::class.java))
    }

    fun launchInviteFlow(activity: Activity?) {
        activity?.startActivity(Intent(activity, InviteActivity::class.java))
    }

    fun launchCreationFlow(activity: Activity?) {
        activity?.startActivity(Intent(activity, AddBetActivity::class.java))
    }

    fun makeAcceptInvitationIntent(context: Context, userId: String): PendingIntent {
        return AcceptInvitationService.makePendingIntent(context, userId)
    }

    fun makeHomeScreenIntent(context: Context): PendingIntent {
        val intent = Intent(context, HomeActivity::class.java)

        return PendingIntent.getActivity(context, 0, intent, 0)
    }
}