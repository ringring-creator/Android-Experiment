package com.ring.ring.todo.feature.list

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoLocalDataSource
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.todo.infra.test.FakeErrorTodoLocalDataSource
import com.ring.ring.todo.infra.test.FakeErrorTodoNetworkDataSource
import com.ring.ring.todo.infra.test.FakeTodoLocalDataSource
import com.ring.ring.todo.infra.test.FakeTodoNetworkDataSource
import com.ring.ring.user.infra.model.UserLocalDataSource
import com.ring.ring.user.infra.test.FakeUserLocalDataSource
import com.ring.ring.user.infra.test.TestActivity
import com.ring.ring.user.infra.test.userTestData
import com.ring.ring.util.date.DefaultDateUtil
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class TodoListScreenKtTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    private var todoList = (1L..10L).map { createTodo(id = it) }.toMutableList()
    private var networkDataSource: TodoNetworkDataSource =
        FakeTodoNetworkDataSource(token = userTestData.token, values = todoList)
    private var localDataSource: TodoLocalDataSource = FakeTodoLocalDataSource()
    private var userLocalDataSource: UserLocalDataSource =
        FakeUserLocalDataSource(user = userTestData)
    private var snackbarHostState: SnackbarHostState = SnackbarHostState()
    private val dateUtil = DefaultDateUtil()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `show todo list fetched from repository`() {
        //given,when
        setupTodoListScreen()

        //then
        composeTestRule.onNodeWithText("fakeTitle1").assertExists()
        composeTestRule.onAllNodesWithTag("DoneCheckBox").onFirst().assertIsOn()
        composeTestRule.onAllNodesWithText("Deadline: 2024-01-01").onFirst().assertExists()
        composeTestRule.onNodeWithText("fakeTitle2").assertExists()
    }

    @Test
    fun `show snackbar when fail to fetch from repository`() {
        //given,when
        networkDataSource = FakeErrorTodoNetworkDataSource()
        localDataSource = FakeErrorTodoLocalDataSource()
        setupTodoListScreen()
        println(snackbarHostState.currentSnackbarData)

        //then
        composeTestRule
            .onNodeWithText("Failed to get todo list")
            .assertExists()
    }

    @Test
    fun `scroll todo list`() {
        //given
        setupTodoListScreen()
        composeTestRule.onNodeWithText("fakeTitle10").assertDoesNotExist()

        //when
        composeTestRule
            .onNodeWithTag("Content")
            .performScrollToIndex(9)

        //then
        composeTestRule.onNodeWithText("fakeTitle10").assertExists()
    }

    @Test
    fun `tapped toggleDone checkbox toggled`() {
        //given
        setupTodoListScreen()

        //when
        composeTestRule
            .onAllNodesWithTag("DoneCheckBox")
            .onFirst()
            .performClick()

        //then
        composeTestRule
            .onAllNodesWithTag("DoneCheckBox")
            .onFirst()
            .assertIsOff()
    }

    @Test
    fun `tapped toggleDone edit todo`() {
        //given
        setupTodoListScreen()
        runBlocking {
            val todo = networkDataSource.fetch(1L, userTestData.token)
            assertThat(todo.done, `is`(true))
        }

        //when
        composeTestRule
            .onAllNodesWithTag("DoneCheckBox")
            .onFirst()
            .performClick()

        //then
        runBlocking {
            val todo = networkDataSource.fetch(1L, userTestData.token)
            assertThat(todo.done, `is`(false))
        }
    }

    @Test
    fun `tapped toggleDone show snackbar when failed to edit done`() {
        //given
        setupTodoListScreen()
        runBlocking { networkDataSource.delete(1L, userTestData.token) }

        //when
        composeTestRule
            .onAllNodesWithTag("DoneCheckBox")
            .onFirst()
            .performClick()

        //then
        composeTestRule
            .onNodeWithText("Failed to edit done")
            .assertExists()
    }

    @Test
    fun `tapped todo navigation edit todo screen`() {
        //given
        var actual = ""
        setupTodoListScreen(toEditTodoScreen = { actual = it })

        //when
        composeTestRule
            .onAllNodesWithTag("Item", true)
            .onFirst()
            .performClick()

        //then
        assertThat(actual, equalTo("1"))
    }

    @Test
    fun `tapped createTodoActionButton navigation create todo screen`() {
        //given
        var wasCalled = false
        setupTodoListScreen(toCreateTodoScreen = { wasCalled = true })

        //when
        composeTestRule
            .onNodeWithTag("CreateTodoActionButton")
            .performClick()

        //then
        assertThat(wasCalled, `is`(true))
    }

    private fun setupTodoListScreen(
        toCreateTodoScreen: () -> Unit = {},
        toEditTodoScreen: (id: String) -> Unit = {},
        toMyPageScreen: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            TodoListScreen(
                viewModel = TodoListViewModel(
                    todoRepository = TodoListRepository(
                        networkDataSource = networkDataSource,
                        localDataSource = localDataSource,
                        userLocalDataSource = userLocalDataSource,
                        dateUtil = dateUtil
                    ),
                ),
                toCreateTodoScreen = toCreateTodoScreen,
                toEditTodoScreen = toEditTodoScreen,
                toMyPageScreen = toMyPageScreen,
                snackbarHostState = snackbarHostState,
            )
        }
    }

    private fun createTodo(id: Long): Todo = Todo(
        id = id,
        title = "fakeTitle$id",
        description = "",
        done = true,
        deadline = Instant.parse("2024-01-01T00:00:00Z"),
    )
}