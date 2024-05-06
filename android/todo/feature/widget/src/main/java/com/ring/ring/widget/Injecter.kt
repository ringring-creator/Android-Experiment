package com.ring.ring.widget

import android.content.Context
import com.ring.ring.todo.infra.domain.TodoLocalDataSource
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.user.infra.model.UserLocalDataSource
import com.ring.ring.util.date.DateUtil
import dagger.hilt.android.EntryPointAccessors

object Injecter {
    fun getNetworkDataSource(context: Context): TodoNetworkDataSource {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context, WidgetEntryPoint::class.java
        )
        return hiltEntryPoint.networkDataSource()
    }

    fun getLocalDataSource(context: Context): TodoLocalDataSource {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context, WidgetEntryPoint::class.java
        )
        return hiltEntryPoint.localDataSource()
    }

    fun getUserLocalDataSource(context: Context): UserLocalDataSource {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context, WidgetEntryPoint::class.java
        )
        return hiltEntryPoint.userLocalDataSource()
    }

    fun getDateUtil(context: Context): DateUtil {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context, WidgetEntryPoint::class.java
        )
        return hiltEntryPoint.dateUtil()
    }
}