package com.ring.ring.user.infra.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ring.ring.user.infra.local.UserDataStoreDataSource
import com.ring.ring.user.infra.model.UserLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Modules {
    @Provides
    @Singleton
    fun providesUserLocalDataSource(
        localDataSource: UserDataStoreDataSource
    ): UserLocalDataSource = localDataSource

    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore("user-settings")

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.userDataStore
}