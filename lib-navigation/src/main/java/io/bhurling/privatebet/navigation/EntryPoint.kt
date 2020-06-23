package io.bhurling.privatebet.navigation

import android.content.Context
import android.content.Intent
import java.io.Serializable

private const val KEY_PARAMS = "io.bhurling.privatebet.navigation.EXTRA_START_PARAMS"

enum class EntryPoint(
    val classPath: String,
    val params: ActivityStartParams? = null
) {

    Friends(
        classPath = "io.bhurling.privatebet.home.HomeActivity",
        params = HomeActivityStartParams(
            defaultToFriends = true
        )
    ),
    CreateBet(classPath = "io.bhurling.privatebet.add.AddBetActivity"),
    Invite(classPath = "io.bhurling.privatebet.friends.InviteActivity"),
    Home(classPath = "io.bhurling.privatebet.home.HomeActivity"),
    Signup(classPath = "io.bhurling.privatebet.signup.SignupActivity")
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