package hu.bme.aut.android.examapp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.examapp.data.repositories.inrefaces.UserRepository
import hu.bme.aut.android.examapp.data.repositories.offline.OfflineUserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepository: OfflineUserRepository
    ): UserRepository

}