package io.bhurling.privatebet.feed

import io.bhurling.privatebet.Qualifiers
import org.koin.dsl.module.applicationContext

val feedKoinModule = applicationContext {

    factory {
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

    factory {
        FeedAdapter(
            interactor = get()
        )
    }

    factory {
        GetBetInteractor(
            firebase = get(),
            bets = get(Qualifiers.bets)
        )
    }
}