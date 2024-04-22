package com.ring.ring.todo.feature.create.viewmodel

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
import kotlinx.datetime.Instant
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateTodoViewModelTest {
    private lateinit var subject: CreateTodoViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private var networkDataSource: TodoNetworkDataSource =
        FakeTodoNetworkDataSource(userTestData.token)
    private var userLocalDataSource: UserLocalDataSource =
        FakeUserLocalDataSource(user = userTestData)
    private val dateUtil = DefaultDateUtil()

    @Before
    fun setUp() {
        setupSubject()
    }

    @Test
    fun `saveTodo request in network`() = runTest {
        //given
        subject.setDeadline(1L)

        //when
        subject.saveTodo()
        advanceUntilIdle()

        //then
        val element = networkDataSource.fetchList(userTestData.token)
            .find { it.deadline == Instant.fromEpochMilliseconds(1L) }!!
        assertThat(element, notNullValue())
    }

    @Test
    fun `saveTodo send UnauthorizedError when user does not saved`() = runTest {
        //given
        userLocalDataSource = mockk(relaxed = true) {
            coEvery { getUser() } returns null
        }
        setupSubject()
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.event.collect {
                if (it == CreateTodoEvent.UnauthorizedError) wasCalled = true
            }
        }

        //when
        subject.saveTodo()
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun setDeadline() {
        //given,when
        val expected = 1L
        subject.setDeadline(expected)

        //then
        assertThat(
            subject.uiState.value.deadline,
            equalTo(dateUtil.format(dateUtil.toInstant(expected)))
        )
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

    private fun setupSubject() {
        subject = CreateTodoViewModel(
            todoRepository = CreateTodoRepository(
                networkDataSource = networkDataSource,
                userLocalDataSource = userLocalDataSource,
            ),
            dateUtil = dateUtil,
        )
    }
}
