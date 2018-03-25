package io.bhurling.privatebet

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import io.bhurling.privatebet.rx.ReactiveFirebase
import org.koin.dsl.module.applicationContext

val applicationKoinModule = applicationContext {

    provide {
        FirebaseDatabase.getInstance()
    }

    provide {
        FirebaseAuth.getInstance()
    }

    factory {
        ReactiveFirebase()
    }

    factory {
        get<FirebaseAuth>().currentUser!! // TODO nullable?
    }

    factory(Qualifiers.feed) {
        get<FirebaseDatabase>()
                .getReference("feeds")
                .child(get<FirebaseUser>().uid)
    }

    factory(Qualifiers.bets) {
        get<FirebaseDatabase>()
                .getReference("bets")
    }

    factory(Qualifiers.me) {
        get<FirebaseDatabase>()
                .getReference("profiles")
                .child(get<FirebaseUser>().uid)
    }
}

object Qualifiers {
    const val bets = "bets"
    const val feed = "feed"
    const val me = "me"
}
