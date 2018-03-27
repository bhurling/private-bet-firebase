package io.bhurling.privatebet.friends

import org.koin.dsl.module.applicationContext

val friendsKoinModule = applicationContext {

    factory {
        PeopleInteractor()
    }

    factory {
        InvitePresenter(
                peopleInteractor = get()
        )
    }
}