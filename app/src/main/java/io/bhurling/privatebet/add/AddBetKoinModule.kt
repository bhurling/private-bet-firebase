package io.bhurling.privatebet.add

import org.koin.dsl.module.applicationContext

val addBetKoinModule = applicationContext {
    factory {
        AddBetPresenter()
    }
}