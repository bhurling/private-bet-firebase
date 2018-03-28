package io.bhurling.privatebet

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import io.bhurling.privatebet.rx.ReactiveFirebase
import org.koin.dsl.module.applicationContext

val applicationKoinModule = applicationContext {

    provide {
        Navigator()
    }

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

    factory(Qualifiers.profiles) {
        get<FirebaseDatabase>()
                .getReference("profiles")
                .child("public")
    }

    factory(Qualifiers.Me.public) {
        get<FirebaseDatabase>()
                .getReference("profiles")
                .child("public")
                .child(get<FirebaseUser>().uid)
    }

    factory(Qualifiers.Me.private) {
        get<FirebaseDatabase>()
                .getReference("profiles")
                .child("private")
                .child(get<FirebaseUser>().uid)
    }
}

object Qualifiers {
    const val bets = "bets"
    const val feed = "feed"
    const val profiles = "profiles"

    object Me {
        const val private = "me.private"
        const val public = "me.public"
    }
}
