package com.ring.ring.log.di

import com.ring.ring.log.DefaultLogger
import com.ring.ring.log.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {
    @Provides
    @Singleton
    fun providesLogger(
        logger: DefaultLogger
    ): Logger = logger
}