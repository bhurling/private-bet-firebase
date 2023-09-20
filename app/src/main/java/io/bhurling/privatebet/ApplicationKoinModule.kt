package io.bhurling.privatebet

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.bhurling.privatebet.rx.firebase.ReactiveFirebase
import javax.inject.Qualifier
import javax.inject.Singleton

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
    @MePublicDocument
    fun provideMePublicDoc(store: FirebaseFirestore, auth: FirebaseAuth) =
        store.collection("public_profiles")
            .document(auth.currentUser?.uid ?: "")

    @Provides
    @MePrivateDocument
    fun provideMePrivateDoc(store: FirebaseFirestore, auth: FirebaseAuth) =
        store.collection("private_profiles")
            .document(auth.currentUser?.uid ?: "")

    @Provides
    @DeviceDocument
    fun provideDeviceDoc(store: FirebaseFirestore, auth: FirebaseAuth) =
        store.collection("devices")
            .document(auth.currentUser?.uid ?: "")

    @Provides
    @FeedCollection
    fun provideFeedCollection(store: FirebaseFirestore, auth: FirebaseAuth) =
        store.collection("feeds")
            .document(auth.currentUser?.uid ?: "")
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