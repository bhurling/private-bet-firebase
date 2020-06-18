package io.bhurling.privatebet.navigation

import android.content.Context
import android.content.Intent
import java.io.Serializable

private const val KEY_PARAMS = "io.bhurling.privatebet.navigation.EXTRA_START_PARAMS"

sealed class EntryPoint(
    val classPath: String,
    val params: ActivityStartParams? = null
) {

    object Friends : EntryPoint(
        classPath = "io.bhurling.privatebet.home.HomeActivity",
        params = HomeActivityStartParams(
            defaultToFriends = true
        )
    )

    object CreateBet : EntryPoint(
        classPath = "io.bhurling.privatebet.add.AddBetActivity"
    )

    object Invite : EntryPoint(
        classPath = "io.bhurling.privatebet.friends.InviteActivity"
    )

    object Home : EntryPoint(
        classPath = "io.bhurling.privatebet.home.HomeActivity"
    )

    object Signup : EntryPoint(
        classPath = "io.bhurling.privatebet.signup.SignupActivity"
    )
}

abstract class ActivityStartParams : Serializable {
    companion object {
        fun <T : ActivityStartParams> from(intent: Intent) : T? {
            return intent.getSerializableExtra(KEY_PARAMS) as? T
        }
    }
}

fun EntryPoint.makeIntent(context: Context): Intent {
    return Intent(context, Class.forName(classPath))
        .apply {
            params?.let {
                putExtra(KEY_PARAMS, it)
            }
        }
}

fun EntryPoint.launch(context: Context) {
    context.startActivity(makeIntent(context))
}