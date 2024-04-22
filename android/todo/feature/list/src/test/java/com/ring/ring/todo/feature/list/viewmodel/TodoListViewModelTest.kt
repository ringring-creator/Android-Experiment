package com.ring.ring.todo.feature.list.viewmodel

import com.ring.ring.infra.test.MainDispatcherRule
import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoLocalDataSource
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.todo.infra.test.FakeErrorTodoNetworkDataSource
import com.ring.ring.todo.infra.test.FakeTodoLocalDataSource
import com.ring.ring.todo.infra.test.FakeTodoNetworkDataSource
import com.ring.ring.user.infra.model.UserLocalDataSource
import com.ring.ring.user.infra.test.FakeUserLocalDataSource
import com.ring.ring.user.infra.test.userTestData
import com.ring.ring.util.date.DefaultDateUtil
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodoListViewModelTest {
    private lateinit var subject: TodoListViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private var networkTodoList = listOf(
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
    private var localTodoList = listOf(
        Todo(
            id = 3,
            title = "fakeTitle3",
            description = "fakeDescription3",
            done = false,
            deadline = Instant.parse("2024-01-01T00:00:00Z"),
        ),
    )
    private var networkDataSource: TodoNetworkDataSource = FakeTodoNetworkDataSource(
        userTestData.token, networkTodoList.toMutableList()
    )
    private var localDataSource: TodoLocalDataSource =
        FakeTodoLocalDataSource(localTodoList.toMutableList())
    private var userLocalDataSource: UserLocalDataSource = FakeUserLocalDataSource(userTestData)
    private val dateUtil = DefaultDateUtil()

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
        val actual = localDataSource.load()

        assertThat(actual.count(), equalTo(2))
        val firstElement = actual.first()
        val expected = networkTodoList.first()
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
        assertThat(todoList.count(), equalTo(1))

        val firstElement = todoList.first()
        val expected = localTodoList.first()
        assertThat(firstElement.id, equalTo(expected.id))
        assertThat(firstElement.title, equalTo(expected.title))
        assertThat(firstElement.done, equalTo(expected.done))
        assertThat(firstElement.deadline, equalTo(dateUtil.format(expected.deadline)))
    }

    @Test
    fun `editDone send UnauthorizedError when user does not saved`() = runTest {
        //given
        userLocalDataSource = mockk(relaxed = true) {
            coEvery { getUser() } returns null
        }
        setupSubject()
        subject.fetchTodoList()

        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.event.collect { wasCalled = true }
        }

        //when
        subject.toggleDone(localTodoList.first().id!!)
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
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
