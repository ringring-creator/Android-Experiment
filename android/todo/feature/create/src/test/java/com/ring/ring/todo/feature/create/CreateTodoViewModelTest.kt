package com.ring.ring.todo.feature.create

import com.ring.ring.infra.test.MainDispatcherRule
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.todo.infra.test.FakeErrorTodoNetworkDataSource
import com.ring.ring.todo.infra.test.FakeTodoNetworkDataSource
import com.ring.ring.user.infra.model.User
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

    private var localUser = User(10L, "fakeEmail", "fakeToken")
    private var networkDataSource: TodoNetworkDataSource = FakeTodoNetworkDataSource(
        parameter = FakeTodoNetworkDataSource.Parameter(localUser.id, localUser.token)
    )
    private var userLocalDataSource = FakeUserLocalDataSource()


    @Before
    fun setUp() {
        runBlocking { userLocalDataSource.save(localUser) }


        setupSubject()
    }

    @Test
    fun saveTodo() = runTest {
        //given
        subject.setTitle("fakeTitle3")
        subject.setDescription("fakeDescription3")
        subject.setDone(true)
        subject.setDeadline(1L)

        //when
        subject.saveTodo()
        advanceUntilIdle()

        //then
        val element = networkDataSource.list(localUser.token).find { it.title == "fakeTitle3" }!!
        assertThat(element.title, equalTo("fakeTitle3"))
        assertThat(element.description, equalTo("fakeDescription3"))
        assertThat(element.done, equalTo(true))
        assertThat(element.deadline, equalTo(Instant.fromEpochMilliseconds(1L)))
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
        networkDataSource = FakeErrorTodoNetworkDataSource()
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
