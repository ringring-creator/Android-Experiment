package com.ring.ring.todo.feature.create

import com.ring.ring.infra.test.MainDispatcherRule
import com.ring.ring.todo.infra.network.dto.CreateRequest
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
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateTodoViewModelTest {
    private lateinit var subject: CreateTodoViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private var networkDataSource = FakeTodoNetworkDataSource()
    private var userLocalDataSource = FakeUserLocalDataSource()

    private var localUser = LocalUser(10L, "fakeEmail", "fakeToken")

    @Before
    fun setUp() {
        runBlocking { userLocalDataSource.save(localUser) }
        networkDataSource.parameter =
            FakeTodoNetworkDataSource.Parameter(localUser.userId, localUser.token)

        setupSubject()
    }

    @Test
    fun saveTodo() = runTest {
        //given
        subject.setTitle("fakeTitle")
        subject.setDescription("fakeDescription")
        subject.setDone(true)
        subject.setDeadline(1L)

        //when
        subject.saveTodo()
        advanceUntilIdle()

        //then
        val expected = FakeTodoNetworkDataSource.CreateParameter(
            request = CreateRequest(
                title = "fakeTitle",
                description = "fakeDescription",
                done = true,
                deadline = Instant.fromEpochMilliseconds(1L),
            ),
            token = "fakeToken"
        )
        assertThat(networkDataSource.createWasCalled, equalTo(expected))
    }

    @Test
    fun `saveTodo send saveSuccessEvent`() = runTest {
        //given
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.saveSuccessEvent.collect {
                wasCalled = true
            }
        }

        //when
        subject.saveTodo()
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun `saveTodo send saveTodoErrorEvent when failed`() = runTest {
        //given
        networkDataSource = FakeTodoNetworkDataSource(true)
        setupSubject()
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.saveTodoErrorEvent.collect {
                wasCalled = true
            }
        }

        //when
        subject.saveTodo()
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun setTitle() {
        //given,when
        val expected = "fakeTitle"
        subject.setTitle(expected)

        //then
        assertThat(subject.title.value, equalTo(expected))
    }

    @Test
    fun setDescription() {
        //given,when
        val expected = "fakeDescription"
        subject.setDescription(expected)

        //then
        assertThat(subject.description.value, equalTo(expected))
    }

    @Test
    fun setDone() {
        //given,when
        val expected = true
        subject.setDone(expected)

        //then
        assertThat(subject.done.value, equalTo(expected))
    }

    @Test
    fun setDeadline() {
        //given,when
        val expected = 1L
        subject.setDeadline(expected)

        //then
        assertThat(subject.deadline.value, equalTo(CreateTodoUiState.Deadline(expected)))
    }

    @Test
    fun showDatePicker() {
        //given,when
        subject.showDatePicker()

        //then
        assertThat(subject.isShowDatePicker.value, `is`(true))
    }

    @Test
    fun dismissDatePicker() {
        //given,when
        subject.dismissDatePicker()

        //then
        assertThat(subject.isShowDatePicker.value, `is`(false))
    }

    private fun setupSubject() {
        subject = CreateTodoViewModel(
            todoRepository = TodoRepository(
                networkDataSource = networkDataSource,
                userLocalDataSource = userLocalDataSource,
            ),
        )
    }
}