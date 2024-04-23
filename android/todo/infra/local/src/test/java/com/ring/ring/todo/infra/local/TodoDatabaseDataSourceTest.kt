package com.ring.ring.todo.infra.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ring.ring.infra.db.AndroidExperimentDatabase
import com.ring.ring.todo.infra.domain.Todo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TodoDatabaseDataSourceTest {
    private lateinit var subject: TodoDatabaseDataSource

    private val context: Context = ApplicationProvider.getApplicationContext()
    private lateinit var db: AndroidExperimentDatabase

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            context,
            AndroidExperimentDatabase::class.java,
        ).build()

        subject = TodoDatabaseDataSource(db.todoDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun upsert() = runTest {
        //given
        val expected = Todo(
            id = null,
            title = "fakeTitle",
            description = "fakeDescription",
            done = true,
            deadline = Instant.parse("2021-01-01T00:00:00Z"),
        )

        //when
        subject.upsert(listOf(expected, expected))

        //then
        val actual = subject.getTodoListStream().first()
        assertThat(actual.count(), equalTo(2))
        val firstElement = actual.first()
        assertThat(firstElement.id, equalTo(1L))
        assertThat(firstElement.title, equalTo(expected.title))
        assertThat(firstElement.description, equalTo(expected.description))
        assertThat(firstElement.done, equalTo(expected.done))
        assertThat(firstElement.deadline, equalTo(expected.deadline))
    }

    @Test
    fun deleteAll() = runTest {
        //given
        val expected = Todo(
            id = null,
            title = "fakeTitle",
            description = "fakeDescription",
            done = true,
            deadline = Instant.parse("2021-01-01T00:00:00Z"),
        )
        subject.upsert(listOf(expected, expected))
        var numberOfRecords = subject.getTodoListStream().first().count()
        assertThat(numberOfRecords, equalTo(2))

        //when
        subject.deleteAll()

        //then
        numberOfRecords = subject.getTodoListStream().first().count()
        assertThat(numberOfRecords, equalTo(0))
    }
}