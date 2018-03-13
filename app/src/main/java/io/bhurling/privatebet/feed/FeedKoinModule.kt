package io.bhurling.privatebet.feed

import io.bhurling.privatebet.Qualifiers
import org.koin.dsl.module.applicationContext

val feedKoinModule = applicationContext {
    factory {
        FeedPresenter(
                firebase = get(),
                feed = get(Qualifiers.feed)
        )
    }

    factory {
        FeedAdapter(
                firebase = get(),
                bets = get(Qualifiers.bets)
        )
    }
}