package com.ring.ring.login.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ring.ring.login.BuildConfig
import com.ring.ring.login.DataStoreUserDataSource
import com.ring.ring.login.DefaultUserRepository
import com.ring.ring.login.LoginNetworkDataSource
import com.ring.ring.login.RetrofitLoginDataSource
import com.ring.ring.login.UserLocalDataSource
import com.ring.ring.login.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

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

    @Provides
    @Singleton
    fun providesLoginNetworkDataSource(
        networkDataSource: RetrofitLoginDataSource
    ): LoginNetworkDataSource = networkDataSource

    @Provides
    @Singleton
    fun providesUserLocalDataSource(
        localDataSource: DataStoreUserDataSource
    ): UserLocalDataSource = localDataSource

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.dataStore

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                },
        )
        .build()
}