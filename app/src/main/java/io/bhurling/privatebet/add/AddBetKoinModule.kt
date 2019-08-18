package io.bhurling.privatebet.add

import io.bhurling.privatebet.Qualifiers
import org.koin.dsl.module.applicationContext

val addBetKoinModule = applicationContext {
    factory {
        AddBetViewModel(
                interactor = get()
        )
    }

    factory {
        OpponentsAdapter(
                firebase = get(),
                profiles = get(Qualifiers.profiles)
        )
    }
}