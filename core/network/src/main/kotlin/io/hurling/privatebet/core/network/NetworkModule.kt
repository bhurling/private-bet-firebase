package io.hurling.privatebet.core.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    @Provides
    @Singleton
    fun provideFirestoreSettings() = FirebaseFirestoreSettings.Builder().build()

    @Provides
    @Singleton
    fun provideFirestore(settings: FirebaseFirestoreSettings) =
        FirebaseFirestore.getInstance().apply {
            firestoreSettings = settings
        }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModuleBinder {
    @Binds
    internal abstract fun bindsNetworkProfilesDataSource(
        dataSource: FirestoreDataSource
    ) : NetworkProfilesDataSource

    @Binds
    internal abstract fun bindsNetworkLinksDataSource(
        dataSource: FirestoreDataSource
    ) : NetworkLinksDataSource
}
