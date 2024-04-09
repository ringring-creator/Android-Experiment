package com.ring.ring.login.di

import com.ring.ring.login.DefaultUserRepository
import com.ring.ring.login.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModules {
    @Provides
    @Singleton
    fun providesUserRepository(
        userRepository: DefaultUserRepository
    ): UserRepository {
        return userRepository
    }
}