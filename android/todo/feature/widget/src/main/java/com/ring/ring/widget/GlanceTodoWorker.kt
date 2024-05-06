package com.ring.ring.widget

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoLocalDataSource
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.user.infra.model.UserLocalDataSource
import com.ring.ring.util.date.DateUtil
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit


class GlanceTodoWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {
    private val networkDataSource: TodoNetworkDataSource = Injecter.getNetworkDataSource(context)
    private val localDataSource: TodoLocalDataSource = Injecter.getLocalDataSource(context)
    private val userLocalDataSource: UserLocalDataSource = Injecter.getUserLocalDataSource(context)
    private val dateUtil: DateUtil = Injecter.getDateUtil(context)

    private val manager = GlanceAppWidgetManager(context)

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "doWork")
            val uiState = generateUiState()
            val encodedUiState = Json.encodeToString(uiState)
            Log.d(TAG, "encodedUiState:$encodedUiState")
            manager.getGlanceIds(MyAppWidget::class.java).forEach { glanceId ->
                updateAppWidgetState(context, glanceId) { prefs ->
                    prefs[TODO_LIST_KEY] = encodedUiState
                    Log.d(TAG, "save uiState for $glanceId")
                }
            }
            MyAppWidget().updateAll(context)
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "e:$e")
            if (runAttemptCount < 10) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    private suspend fun generateUiState(): GlanceTodoUiState {
        val todoList = userLocalDataSource.getUser()?.token?.let {
            networkDataSource.fetchList(it)
        } ?: run {
            localDataSource.getTodoListStream().first()
        }
        val uiState = GlanceTodoUiState(todoList.mapNotNull(this::convert))
        return uiState
    }

    companion object {
        val TODO_LIST_KEY = stringPreferencesKey("todo_list")
        private const val TAG = "GlanceTodoWorker"
        private val uniqueWorkName = GlanceTodoWorker::class.java.simpleName

        fun enqueue(context: Context, glanceId: GlanceId, force: Boolean = false) {
            val manager = WorkManager.getInstance(context)
            val requestBuilder = OneTimeWorkRequestBuilder<GlanceTodoWorker>().apply {
                addTag(glanceId.toString())
                setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            }
            val workPolicy = if (force) {
                ExistingWorkPolicy.REPLACE
            } else {
                ExistingWorkPolicy.KEEP
            }

            manager.enqueueUniqueWork(
                uniqueWorkName,
                workPolicy,
                requestBuilder.build(),
            )

            manager.enqueueUniqueWork(
                "$uniqueWorkName-workaround",
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequestBuilder<GlanceTodoWorker>().apply {
                    setInitialDelay(365, TimeUnit.DAYS)
                }.build(),
            )
        }

        fun cancel(context: Context, glanceId: GlanceId) {
            WorkManager.getInstance(context).cancelAllWorkByTag(glanceId.toString())
        }
    }

    private fun convert(todo: Todo): GlanceTodoUiState.Todo? {
        return todo.id?.let {
            GlanceTodoUiState.Todo(
                id = it,
                title = todo.title,
                done = todo.done,
                deadline = dateUtil.format(todo.deadline),
            )
        }
    }
}
