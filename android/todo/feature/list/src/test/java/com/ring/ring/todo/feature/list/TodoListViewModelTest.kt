package com.ring.ring.todo.feature.list

import com.ring.ring.infra.test.MainDispatcherRule
import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoLocalDataSource
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.todo.infra.test.FakeErrorTodoNetworkDataSource
import com.ring.ring.todo.infra.test.FakeTodoLocalDataSource
import com.ring.ring.todo.infra.test.FakeTodoNetworkDataSource
import com.ring.ring.user.infra.model.User
import com.ring.ring.user.infra.test.FakeUserLocalDataSource
import com.ring.ring.util.date.DateUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodoListViewModelTest {
    private lateinit var subject: TodoListViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private var user = User.generate(10L, "email@example.com", "fakeToken")
    private var todoList = listOf(
        Todo(
            id = 1,
            title = "fakeTitle",
            description = "fakeDescription",
            done = false,
            deadline = Instant.parse("2024-01-01T00:00:00Z"),
        ),
        Todo(
            id = 2,
            title = "fakeTitle2",
            description = "fakeDescription2",
            done = true,
            deadline = Instant.parse("2024-12-31T00:00:00Z"),
        ),
    )
    private var networkDataSource: TodoNetworkDataSource = FakeTodoNetworkDataSource(
        user.token, todoList.toMutableList()
    )
    private var localDataSource: TodoLocalDataSource =
        FakeTodoLocalDataSource(todoList.toMutableList())
    private var userLocalDataSource = FakeUserLocalDataSource(user)
    private val dateUtil = DateUtil()

    @Before
    fun setUp() {
        setupSubject()
    }

    @Test
    fun `fetchTodoList set todoList fetched from network`() = runTest {
        //given,when
        subject.fetchTodoList()
        advanceUntilIdle()

        //then
        val todoList = subject.uiState.value.todoList
        assertThat(todoList.count(), equalTo(2))

        val firstElement = todoList.first()
        assertThat(firstElement.id, equalTo(1))
        assertThat(firstElement.title, equalTo("fakeTitle"))
        assertThat(firstElement.done, equalTo(false))
        assertThat(firstElement.deadline, equalTo("2024-01-01"))
    }

    @Test
    fun `fetchTodoList save local todoList fetched from network`() = runTest {
        //given,when
        subject.fetchTodoList()
        advanceUntilIdle()

        //then
        val actual = localDataSource.list()

        assertThat(actual.count(), equalTo(2))
        val firstElement = actual.first()
        val expected = networkDataSource.list(user.token).first()
        assertThat(firstElement.id, equalTo(1))
        assertThat(firstElement.title, equalTo(expected.title))
        assertThat(firstElement.description, equalTo(expected.description))
        assertThat(firstElement.done, equalTo(expected.done))
        assertThat(firstElement.deadline, equalTo(expected.deadline))
    }

    @Test
    fun `fetchTodoList set todoList fetched from local when network failed`() = runTest {
        //given
        networkDataSource = FakeErrorTodoNetworkDataSource()
        setupSubject()

        //when
        subject.fetchTodoList()
        advanceUntilIdle()

        //then
        val todoList = subject.uiState.value.todoList
        assertThat(todoList.count(), equalTo(2))

        val firstElement = todoList.first()
        val expected = localDataSource.list().first()
        assertThat(firstElement.id, equalTo(expected.id))
        assertThat(firstElement.title, equalTo(expected.title))
        assertThat(firstElement.done, equalTo(expected.done))
        assertThat(firstElement.deadline, equalTo(dateUtil.format(expected.deadline)))
    }

    private fun setupSubject() {
        subject = TodoListViewModel(
            todoRepository = TodoListRepository(
                networkDataSource = networkDataSource,
                localDataSource = localDataSource,
                userLocalDataSource = userLocalDataSource,
                dateUtil = dateUtil
            ),
        )
    }
}
