package io.bhurling.privatebet.add

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val addBetKoinModule = module {
    viewModel {
        AddBetViewModel(
            interactor = get()
        )
    }
}