package com.ring.ring.todo.feature.create

import com.ring.ring.infra.test.MainDispatcherRule
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
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

    private var user = User.generate(10L, "email@example.com", "Abcdefg1")
    private var networkDataSource: TodoNetworkDataSource =
        FakeTodoNetworkDataSource(user.token)
    private var userLocalDataSource = FakeUserLocalDataSource(user = user)


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
        val element = networkDataSource.list(user.token)
            .find { it.deadline == Instant.fromEpochMilliseconds(1L) }!!
        assertThat(element, notNullValue())
    }

    @Test
    fun setDeadline() {
        //given,when
        val expected = 1L
        subject.setDeadline(expected)

        //then
        val dateUtil = DateUtil()
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
            todoRepository = TodoRepository(
                networkDataSource = networkDataSource,
                userLocalDataSource = userLocalDataSource,
            ),
            dateUtil = DateUtil(),
        )
    }
}
