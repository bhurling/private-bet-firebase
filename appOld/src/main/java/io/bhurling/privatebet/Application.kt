package io.bhurling.privatebet

import dagger.hilt.android.HiltAndroidApp
import io.bhurling.privatebet.navigation.dryrun.DryRun
import timber.log.Timber
import timber.log.Timber.DebugTree

@HiltAndroidApp
class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        if (BuildConfig.DEBUG) {
            DryRun.checkEntryPoints()
        }
    }
}
