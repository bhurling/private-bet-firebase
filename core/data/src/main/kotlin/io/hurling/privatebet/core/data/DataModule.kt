package io.hurling.privatebet.core.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsLinksRepository(
        linksRepository: DefaultLinksRepository
    ) : LinksRepository

    @Binds
    internal abstract fun bindsProfilesRepository(
        profilesRepository: DefaultProfilesRepository
    ) : ProfilesRepository
}