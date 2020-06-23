package io.bhurling.privatebet.home

import org.koin.dsl.module.module

val homeKoinModule = module {
    factory {
        HomeViewModel(
                invitationsInteractor = get()
        )
    }
}