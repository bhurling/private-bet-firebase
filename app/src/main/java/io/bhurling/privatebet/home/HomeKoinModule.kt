package io.bhurling.privatebet.home

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val homeKoinModule = module {
    viewModel {
        HomeViewModel(
                invitationsInteractor = get()
        )
    }
}