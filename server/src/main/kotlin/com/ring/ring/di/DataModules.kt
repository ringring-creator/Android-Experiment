package com.ring.ring.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.ring.ring.data.db.DeadlineAdapter
import com.ring.ring.data.db.TodoDataSource
import com.ring.ring.data.db.UserDataSource
import com.ring.ring.data.repository.TodoRepository
import com.ring.ring.data.repository.UserRepository
import data.db.LocalDb
import data.db.TodoTable
import java.util.Properties

object DataModules {
    val db = createDb()
    val todoRepository = createTodoRepository()
    val userRepository = createUserRepository()
    val userDataSource = createUserDataSource()

    private fun createTodoRepository(): TodoRepository = TodoRepository(
        dataSource = createTodoDataSource()
    )

    private fun createTodoDataSource(): TodoDataSource = TodoDataSource(
        queries = db.todoQueries
    )

    private fun createUserRepository(): UserRepository = UserRepository(
        dataSource = createUserDataSource()
    )

    private fun createUserDataSource(): UserDataSource = UserDataSource(
        queries = db.userQueries
    )

    private fun createDb() = LocalDb(
        driver = createSqliteDriver(),
        TodoTableAdapter = createTodoTableAdapter()
    )

    private fun createTodoTableAdapter() = TodoTable.Adapter(
        deadlineAdapter = createDeadlineAdapter()
    )

    private fun createDeadlineAdapter() = DeadlineAdapter()

    private fun createSqliteDriver(): SqlDriver = JdbcSqliteDriver(
        url = "jdbc:sqlite:db/database.db",
        schema = LocalDb.Schema,
        properties = Properties().apply { put("foreign_keys", "true") }
    )
}