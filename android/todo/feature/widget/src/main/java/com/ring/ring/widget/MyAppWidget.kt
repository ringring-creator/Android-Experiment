package com.ring.ring.widget

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import com.ring.ring.widget.GlanceTodoWorker.Companion.TODO_LIST_KEY
import kotlinx.serialization.json.Json

class MyAppWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val pref: Preferences = currentState()
            val uiState = pref[TODO_LIST_KEY]?.let {
                Json.decodeFromString<GlanceTodoUiState>(it)
            } ?: GlanceTodoUiState(emptyList())

            GlanceTheme {
                Box(
                    modifier = GlanceModifier.fillMaxSize()
                        .background(GlanceTheme.colors.background)
                ) {
                    LazyColumn {
                        items(uiState.todoList) { todo ->
                            Item(todo)
                        }
                    }
                }
            }
            SideEffect {
                GlanceTodoWorker.enqueue(context, id, false)
            }
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        GlanceTodoWorker.cancel(context, glanceId)
    }
}

@Composable
private fun Item(
    todo: GlanceTodoUiState.Todo,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            DoneCheckBox(todo.done)
            TitleText(todo.title)
        }
        DeadlineText(
            deadline = todo.deadline,
        )
    }
}

@Composable
private fun DoneCheckBox(
    done: Boolean,
) {
    CheckBox(
        checked = done,
        onCheckedChange = {},
    )
}

@Composable
private fun TitleText(title: String) {
    Text(title)
}

@Composable
private fun DeadlineText(deadline: String) {
    Text(glanceString(R.string.deadline, listOf(deadline)))
}

@Composable
fun glanceString(@StringRes id: Int, vararg args: List<String>): String {
    return LocalContext.current.getString(id, args[0])
}