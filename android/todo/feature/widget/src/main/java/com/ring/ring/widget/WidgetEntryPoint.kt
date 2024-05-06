package com.ring.ring.widget

import com.ring.ring.todo.infra.domain.TodoLocalDataSource
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.user.infra.model.UserLocalDataSource
import com.ring.ring.util.date.DateUtil
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun networkDataSource(): TodoNetworkDataSource
    fun localDataSource(): TodoLocalDataSource
    fun userLocalDataSource(): UserLocalDataSource
    fun dateUtil(): DateUtil
}