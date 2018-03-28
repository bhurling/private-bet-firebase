package io.bhurling.privatebet

import android.app.Activity
import android.content.Intent
import io.bhurling.privatebet.feed.FeedActivity
import io.bhurling.privatebet.signup.SignupActivity
import io.bhurling.privatebet.signup.SignupService

class Navigator {

    fun launchSignupFlow(activity: Activity) {
        activity.startActivity(Intent(activity, SignupActivity::class.java))
    }

    fun launchApp(activity: Activity) {
        activity.startService(Intent(activity, SignupService::class.java))
        activity.startActivity(Intent(activity, FeedActivity::class.java))
    }
}