package io.bhurling.privatebet.friends

import io.bhurling.privatebet.Qualifiers
import org.koin.dsl.module.applicationContext

val friendsKoinModule = applicationContext {

    factory {
        PeopleInteractor(
            firebase = get(),
            profiles = get(Qualifiers.profiles)
        )
    }

    factory {
        FriendsPresenter(
            invitationsInteractor = get()
        )
    }

    factory {
        FriendsAdapter(
            peopleInteractor = get()
        )
    }

    factory {
        InviteViewModel(
            peopleInteractor = get(),
            invitationsInteractor = get(),
            me = get()
        )
    }

    factory {
        InvitationsInteractor(
            firebase = get(),
            links = get(Qualifiers.links),
            me = get()
        )
    }
}