package io.bhurling.privatebet.add

import io.bhurling.privatebet.Qualifiers
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val addBetKoinModule = module {
    viewModel {
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