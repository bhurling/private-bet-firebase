package io.bhurling.privatebet

import android.content.Context
import io.bhurling.privatebet.feed.feedKoinModule
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
                appKoinModule,
                feedKoinModule
        ))
    }

}
