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
        InvitePresenter(
                peopleInteractor = get(),
                invitationsInteractor = get()
        )
    }

    factory {
        InvitationsInteractor(
                firebase = get(),
                incoming = get(Qualifiers.Links.incoming),
                outgoing = get(Qualifiers.Links.outgoing)
        )
    }
}