package com.ring.ring.todo.feature.edit

import androidx.lifecycle.SavedStateHandle
import com.ring.ring.infra.test.MainDispatcherRule
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.todo.infra.test.FakeTodoNetworkDataSource
import com.ring.ring.user.infra.model.User
import com.ring.ring.user.infra.test.FakeUserLocalDataSource
import com.ring.ring.util.date.DateUtil
import kotlinx.coroutines.test.StandardTestDispatcher
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditTodoViewModelTest {
    private lateinit var subject: EditTodoViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private var user = User.generate(10L, "email@example.com", "Abcdefg1")
    private var networkDataSource: TodoNetworkDataSource = FakeTodoNetworkDataSource(user.token)
    private var userLocalDataSource = FakeUserLocalDataSource(user)
    private lateinit var savedStateHandle: SavedStateHandle
    private val dateUtil = DateUtil()
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

    private fun setupSubject() {
        subject = EditTodoViewModel(
            todoRepository = TodoRepository(
                networkDataSource = networkDataSource,
                userLocalDataSource = userLocalDataSource,
            ),
            dateUtil = dateUtil,
            savedStateHandle = savedStateHandle,
        )
    }
}
