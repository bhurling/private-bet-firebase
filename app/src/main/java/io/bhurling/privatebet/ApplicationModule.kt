package io.bhurling.privatebet

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
}
