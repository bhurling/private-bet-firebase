package io.bhurling.privatebet

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import io.bhurling.privatebet.rx.ReactiveFirebase
import org.koin.dsl.module.applicationContext

val applicationKoinModule = applicationContext {

    provide {
        FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
    }

    provide {
        FirebaseFirestore.getInstance().apply {
            firestoreSettings = get()
        }
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
        get<FirebaseFirestore>()
            .collection("feeds")
            .document(get<FirebaseUser>().uid)
            .collection("bets")
    }

    factory(Qualifiers.bets) {
        get<FirebaseFirestore>()
            .collection("bets")
    }

    factory(Qualifiers.links) {
        get<FirebaseFirestore>()
            .collection("links")
    }

    factory(Qualifiers.profiles) {
        get<FirebaseFirestore>()
            .collection("public_profiles")
    }

    factory(Qualifiers.Me.public) {
        get<FirebaseFirestore>()
            .collection("public_profiles")
            .document(get<FirebaseUser>().uid)
    }

    factory(Qualifiers.Me.private) {
        get<FirebaseFirestore>()
            .collection("private_profiles")
            .document(get<FirebaseUser>().uid)
    }

    factory(Qualifiers.devices) {
        get<FirebaseFirestore>()
            .collection("devices")
            .document(get<FirebaseUser>().uid)
    }
}

object Qualifiers {
    const val bets = "bets"
    const val feed = "feed"
    const val profiles = "profiles"
    const val links = "links"
    const val devices = "devices"

    object Me {
        const val private = "me.private"
        const val public = "me.public"
    }
}
