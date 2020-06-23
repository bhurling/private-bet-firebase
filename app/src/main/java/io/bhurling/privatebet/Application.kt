package io.bhurling.privatebet

import android.content.Context
import io.bhurling.privatebet.add.addBetKoinModule
import io.bhurling.privatebet.common.push.pushKoinModule
import io.bhurling.privatebet.feed.feedKoinModule
import io.bhurling.privatebet.friends.friendsKoinModule
import io.bhurling.privatebet.home.homeKoinModule
import io.bhurling.privatebet.navigation.dryrun.DryRun
import io.bhurling.privatebet.signup.signupKoinModule
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree


class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        if (BuildConfig.DEBUG) {
            DryRun.checkEntryPoints()
        }

        setupDependencies()
    }

    private fun setupDependencies() {
        startKoin(
            listOf(
                module {
                    single {
                        applicationContext as Context
                    }
                },
                applicationKoinModule,
                signupKoinModule,
                homeKoinModule,
                friendsKoinModule,
                feedKoinModule,
                addBetKoinModule,
                pushKoinModule
            )
        )
    }

}
