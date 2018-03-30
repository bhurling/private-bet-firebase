package io.bhurling.privatebet

import android.app.Activity
import android.content.Intent
import io.bhurling.privatebet.add.AddBetActivity
import io.bhurling.privatebet.friends.InviteActivity
import io.bhurling.privatebet.signup.SignupActivity
import io.bhurling.privatebet.signup.SignupService

class Navigator {

    fun launchSignupFlow(activity: Activity) {
        activity.startActivity(Intent(activity, SignupActivity::class.java))
    }

    fun launchApp(activity: Activity) {
        activity.startService(Intent(activity, SignupService::class.java))
        activity.startActivity(Intent(activity, MainActivity::class.java))
    }

    fun launchInviteFlow(activity: Activity) {
        activity.startActivity(Intent(activity, InviteActivity::class.java))
    }

    fun launchCreationFlow(activity: Activity) {
        activity.startActivity(Intent(activity, AddBetActivity::class.java))
    }
}