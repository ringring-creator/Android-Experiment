package com.ring.ring.todo.feature.edit

import androidx.lifecycle.SavedStateHandle
import com.ring.ring.infra.test.MainDispatcherRule
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
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
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditTodoViewModelTest {
    private lateinit var subject: EditTodoViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private var networkDataSource: TodoNetworkDataSource =
        FakeTodoNetworkDataSource(userTestData.token)
    private var userLocalDataSource: UserLocalDataSource = FakeUserLocalDataSource(userTestData)
    private lateinit var savedStateHandle: SavedStateHandle
    private val dateUtil = DefaultDateUtil()
    private val id = 1L

    @Before
    fun setUp() {
        savedStateHandle = SavedStateHandle(mapOf(EditTodoNav.ID to id))
        setupSubject()
    }

    @Test
    fun setDeadline() {
        //given,when
        val expected = 1L
        subject.setDeadline(expected)

        //then
        assertThat(subject.uiState.value.todo.deadline, equalTo("1970-01-01"))
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
    fun `getTodo send UnauthorizedError when user does not saved`() = runTest {
        //given
        userLocalDataSource = mockk(relaxed = true) {
            coEvery { getUser() } returns null
        }
        setupSubject()
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.event.collect {
                if (it == EditTodoEvent.UnauthorizedError) wasCalled = true
            }
        }

        //when
        subject.getTodo()
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun `editTodo send UnauthorizedError when user does not saved`() = runTest {
        //given
        userLocalDataSource = mockk(relaxed = true) {
            coEvery { getUser() } returns null
        }
        setupSubject()
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.event.collect {
                if (it == EditTodoEvent.UnauthorizedError) wasCalled = true
            }
        }

        //when
        subject.editTodo()
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun `deleteTodo send UnauthorizedError when user does not saved`() = runTest {
        //given
        userLocalDataSource = mockk(relaxed = true) {
            coEvery { getUser() } returns null
        }
        setupSubject()
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.event.collect {
                if (it == EditTodoEvent.UnauthorizedError) wasCalled = true
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
            todoRepository = EditTodoRepository(
                networkDataSource = networkDataSource,
                userLocalDataSource = userLocalDataSource,
                dateUtil = dateUtil,
            ),
            dateUtil = dateUtil,
            savedStateHandle = savedStateHandle,
        )
    }
}
