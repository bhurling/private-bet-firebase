package io.bhurling.privatebet

import android.content.Context
import io.bhurling.privatebet.feed.feedKoinModule
import io.bhurling.privatebet.friends.friendsKoinModule
import io.bhurling.privatebet.home.homeKoinModule
import io.bhurling.privatebet.signup.signupKoinModule
import org.koin.standalone.StandAloneContext.startKoin

class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()

        setupDependencies()
    }

    private fun setupDependencies() {
        val appContextKoinModule = org.koin.dsl.module.applicationContext {
            provide {
                applicationContext as Context
            }
        }

        startKoin(listOf(
                appContextKoinModule,
                applicationKoinModule,
                signupKoinModule,
                homeKoinModule,
                friendsKoinModule,
                feedKoinModule
        ))
    }

}
