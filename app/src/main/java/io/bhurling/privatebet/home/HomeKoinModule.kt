package io.bhurling.privatebet.home

import org.koin.dsl.module.applicationContext

val homeKoinModule = applicationContext {
    factory {
        HomePresenter(
                invitationsInteractor = get()
        )
    }
}