package io.bhurling.privatebet

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.bhurling.privatebet.rx.ReactiveFirebase
import org.koin.dsl.module.applicationContext

val appKoinModule = applicationContext {

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
}

object Qualifiers {
    const val bets = "bets"
    const val feed = "feed"
}

class ApplicationModule(private val application: Application) {

    fun provideAppContext(): Context {
        return application
    }

}
