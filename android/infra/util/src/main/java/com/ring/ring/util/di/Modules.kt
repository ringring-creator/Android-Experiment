package com.ring.ring.util.di

import com.ring.ring.util.date.DateUtil
import com.ring.ring.util.date.DefaultDateUtil
import com.ring.ring.util.log.DefaultLogger
import com.ring.ring.util.log.Logger
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

    @Provides
    @Singleton
    fun providesDateUtil(): DateUtil = DefaultDateUtil()
}