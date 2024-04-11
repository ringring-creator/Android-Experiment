package com.ring.ring.infra.log.di

import com.ring.ring.infra.log.DefaultLogger
import com.ring.ring.infra.log.Logger
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