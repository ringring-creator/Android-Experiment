package com.ring.ring.infra.db.di

import android.content.Context
import androidx.room.Room
import com.ring.ring.infra.db.AndroidExperimentDatabase
import com.ring.ring.infra.db.TodoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object Module {
    @Provides
    @Singleton
    fun providesAndroidExperimentDatabase(
        @ApplicationContext context: Context,
    ): AndroidExperimentDatabase = Room.databaseBuilder(
        context,
        AndroidExperimentDatabase::class.java,
        "android-experiment-database",
    ).build()

    @Provides
    fun providesTodoDao(
        database: AndroidExperimentDatabase,
    ): TodoDao = database.todoDao()
}
