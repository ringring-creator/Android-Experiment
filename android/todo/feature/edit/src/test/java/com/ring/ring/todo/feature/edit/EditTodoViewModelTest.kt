package com.ring.ring.todo.feature.edit

import androidx.lifecycle.SavedStateHandle
import com.ring.ring.infra.test.MainDispatcherRule
import com.ring.ring.todo.infra.test.FakeTodoNetworkDataSource
import com.ring.ring.user.infra.local.LocalUser
import com.ring.ring.user.infra.test.FakeUserLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditTodoViewModelTest {
    private lateinit var subject: EditTodoViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private var networkDataSource = FakeTodoNetworkDataSource()
    private var userLocalDataSource = FakeUserLocalDataSource()
    private lateinit var savedStateHandle: SavedStateHandle

    private val id = 1L
    private var localUser = LocalUser(10L, "fakeEmail", "fakeToken")

    @Before
    fun setUp() {
        savedStateHandle = SavedStateHandle(mapOf(EditTodoNav.ID to id))
        runBlocking { userLocalDataSource.save(localUser) }
        networkDataSource.parameter =
            FakeTodoNetworkDataSource.Parameter(localUser.userId, localUser.token)

        setupSubject()
    }

    @Test
    fun setTitle() {
        //given,when
        val expected = "fakeTitle"
        subject.setTitle(expected)

        //then
        assertThat(subject.uiState.value.title, equalTo(expected))
    }

    @Test
    fun setDescription() {
        //given,when
        val expected = "fakeDescription"
        subject.setDescription(expected)

        //then
        assertThat(subject.uiState.value.description, equalTo(expected))
    }

    @Test
    fun setDone() {
        //given,when
        val expected = true
        subject.setDone(expected)

        //then
        assertThat(subject.uiState.value.done, equalTo(expected))
    }

    @Test
    fun setDeadline() {
        //given,when
        val expected = 1L
        subject.setDeadline(expected)

        //then
        assertThat(subject.uiState.value.deadline, equalTo("1970-01-01"))
    }

    @Test
    fun showDatePicker() {
        //given,when
        subject.showDatePicker()

        //then
        assertThat(subject.uiState.value.isShowDatePicker, `is`(true))
    }

    @Test
    fun dismissDatePicker() {
        //given,when
        subject.dismissDatePicker()

        //then
        assertThat(subject.uiState.value.isShowDatePicker, `is`(false))
    }

    @Test
    fun `getTodo set todo fetched`() = runTest {
        //given,when
        subject.getTodo()
        advanceUntilIdle()

        //then
        assertThat(subject.uiState.value.title, equalTo("fakeTitle"))
        assertThat(subject.uiState.value.description, equalTo("fakeDescription"))
        assertThat(subject.uiState.value.done, equalTo(false))
        assertThat(subject.uiState.value.deadline, equalTo("2024-01-01"))
    }

    @Test
    fun `getTodo send getTodoErrorEvent when get failed`() = runTest {
        //given
        networkDataSource = FakeTodoNetworkDataSource(true)
        setupSubject()
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.getTodoErrorEvent.collect {
                wasCalled = true
            }
        }

        //when
        subject.getTodo()
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun `editTodo edit Todo via network`() = runTest {
        //given
        subject.setTitle("fakeTitle3")
        subject.setDescription("fakeDescription3")
        subject.setDone(true)
        subject.setDeadline(1L)

        //when
        subject.editTodo()
        advanceUntilIdle()

        //then
        val todo = networkDataSource.listResponse.find { it.id == id }!!
        assertThat(todo.title, equalTo("fakeTitle3"))
        assertThat(todo.description, equalTo("fakeDescription3"))
        assertThat(todo.done, equalTo(true))
        assertThat(todo.deadline, equalTo(Instant.fromEpochMilliseconds(1L)))
    }

    @Test
    fun `editTodo send editFinishedEvent`() = runTest {
        //given
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.editFinishedEvent.collect {
                wasCalled = true
            }
        }

        //when
        subject.editTodo()
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun `editTodo send editErrorEvent when edit failed`() = runTest {
        //given
        networkDataSource = FakeTodoNetworkDataSource(true)
        setupSubject()
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.editErrorEvent.collect {
                wasCalled = true
            }
        }

        //when
        subject.editTodo()
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun `deleteTodo delete Todo via network`() = runTest {
        //given,when
        subject.deleteTodo()
        advanceUntilIdle()

        //then
        val todo = networkDataSource.listResponse.find { it.id == id }
        assertThat(todo, nullValue())
    }

    @Test
    fun `deleteTodo send deleteFinishedEvent`() = runTest {
        //given
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.deleteFinishedEvent.collect {
                wasCalled = true
            }
        }

        //when
        subject.deleteTodo()
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun `deleteTodo send deleteErrorEvent when delete failed`() = runTest {
        //given
        networkDataSource = FakeTodoNetworkDataSource(true)
        setupSubject()
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.deleteErrorEvent.collect {
                wasCalled = true
            }
        }

        //when
        subject.deleteTodo()
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }

    private fun setupSubject() {
        subject = EditTodoViewModel(
            todoRepository = TodoRepository(
                networkDataSource = networkDataSource,
                userLocalDataSource = userLocalDataSource,
            ),
            savedStateHandle = savedStateHandle,
        )
    }
}
