package io.bhurling.privatebet

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.bhurling.privatebet.rx.firebase.ReactiveFirebase
import org.koin.dsl.module.module
import javax.inject.Qualifier
import javax.inject.Singleton

val applicationKoinModule = module {

    single {
        FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
    }

    single {
        FirebaseFirestore.getInstance().apply {
            firestoreSettings = get()
        }
    }

    single {
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

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DeviceDocument

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MePrivateDocument

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MePublicDocument

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BetsCollection

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FeedCollection

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProfilesCollection

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LinksCollection

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


@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    fun provideFirestoreSettings() = FirebaseFirestoreSettings.Builder()
        .setPersistenceEnabled(true)
        .build()

    @Provides
    @Singleton
    fun provideFirestore(settings: FirebaseFirestoreSettings) =
        FirebaseFirestore.getInstance().apply {
            firestoreSettings = settings
        }

    @Provides
    @Singleton
    fun provideAuth() = FirebaseAuth.getInstance()

    @Provides
    fun provideReactiveFirebase() = ReactiveFirebase()

    @Provides
    fun provideCurrentUser(auth: FirebaseAuth) = auth.currentUser!! // TODO nullable?

    @Provides
    @MePublicDocument
    fun provideMePublicDoc(store: FirebaseFirestore, user: FirebaseUser) =
        store.collection("public_profiles")
            .document(user.uid)

    @Provides
    @MePrivateDocument
    fun provideMePrivateDoc(store: FirebaseFirestore, user: FirebaseUser) =
        store.collection("private_profiles")
            .document(user.uid)

    @Provides
    @DeviceDocument
    fun provideDeviceDoc(store: FirebaseFirestore, user: FirebaseUser) =
        store.collection("devices").document(user.uid)

    @Provides
    @FeedCollection
    fun provideFeedCollection(store: FirebaseFirestore, user: FirebaseUser) =
        store.collection("feeds")
            .document(user.uid)
            .collection("bets")

    @Provides
    @BetsCollection
    fun provideBetsCollection(store: FirebaseFirestore) =
        store.collection("bets")

    @Provides
    @LinksCollection
    fun provideLinksCollection(store: FirebaseFirestore) =
        store.collection("links")

    @Provides
    @ProfilesCollection
    fun provideProfilesCollection(store: FirebaseFirestore) =
        store.collection("public_profiles")
}