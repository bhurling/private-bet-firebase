package io.bhurling.privatebet.friends

import io.bhurling.privatebet.Qualifiers
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val friendsKoinModule = module {

    factory {
        PeopleInteractor(
            firebase = get(),
            profiles = get(Qualifiers.profiles)
        )
    }

    viewModel {
        FriendsViewModel(
            invitationsInteractor = get()
        )
    }

    factory {
        FriendsAdapter()
    }

    viewModel {
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