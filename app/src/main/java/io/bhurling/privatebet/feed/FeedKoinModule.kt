package io.bhurling.privatebet.feed

import io.bhurling.privatebet.Qualifiers
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val feedKoinModule = module {

    viewModel {
        FeedViewModel(
            interactor = get()
        )
    }

    factory {
        GetKeysInteractor(
            firebase = get(),
            feed = get(Qualifiers.feed)
        )
    }
}