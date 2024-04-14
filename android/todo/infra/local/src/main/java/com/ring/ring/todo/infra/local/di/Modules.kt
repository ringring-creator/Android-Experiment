package com.ring.ring.todo.infra.local.di

import com.ring.ring.todo.infra.domain.TodoLocalDataSource
import com.ring.ring.todo.infra.local.TodoDatabaseDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesTodoLocalDataSource(
        todoDatabaseDataSource: TodoDatabaseDataSource,
    ): TodoLocalDataSource = todoDatabaseDataSource
}
