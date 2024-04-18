package com.ring.ring.todo.feature.create

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.todo.infra.test.FakeErrorTodoNetworkDataSource
import com.ring.ring.todo.infra.test.FakeTodoNetworkDataSource
import com.ring.ring.user.infra.model.User
import com.ring.ring.user.infra.test.FakeUserLocalDataSource
import com.ring.ring.user.infra.test.TestActivity
import com.ring.ring.util.date.DateUtil
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.runBlocking
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
class CreateTodoScreenKtTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    private var user = User.generate(id = 1L, email = "email@example.com", token = "fakeToken")
    private var networkDataSource: TodoNetworkDataSource =
        FakeTodoNetworkDataSource(token = user.token)
    private var localDataSource = FakeUserLocalDataSource(user = user)
    private val dateUtil = DateUtil()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `tapped createButton save todo`() {
        //given
        setupCreateTodoScreen()
        composeTestRule
            .onNodeWithTag("TitleTextField")
            .performTextInput("title")
        composeTestRule
            .onNodeWithTag("DescriptionTextField")
            .performTextInput("description")
        composeTestRule
            .onNodeWithTag("DoneCheckBox")
            .performClick()

        //when
        composeTestRule
            .onNodeWithTag("CreateButton")
            .performClick()

        //then
        runBlocking {
            val actual = networkDataSource.fetchList(user.token).find { it.title == "title" }!!
            assertThat(actual.description, equalTo("description"))
            assertThat(actual.done, `is`(true))
        }
    }

    @Test
    fun `tapped createButton navigates to TodoListScreen`() {
        //given
        var wasCalled = false
        setupCreateTodoScreen { wasCalled = true }

        //when
        composeTestRule
            .onNodeWithTag("CreateButton")
            .performClick()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun `tapped createButton show snackbar when createTodo fails`() {
        //given
        networkDataSource = FakeErrorTodoNetworkDataSource()
        setupCreateTodoScreen()

        //when
        composeTestRule
            .onNodeWithTag("CreateButton")
            .performClick()

        //then
        composeTestRule
            .onNodeWithText("Failed to create")
            .assertExists()
    }

    private fun setupCreateTodoScreen(
        toTodoListScreen: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            CreateTodoScreen(
                toTodoListScreen = toTodoListScreen,
                viewModel = CreateTodoViewModel(
                    todoRepository = CreateTodoRepository(
                        networkDataSource = networkDataSource,
                        userLocalDataSource = localDataSource
                    ),
                    dateUtil = dateUtil
                )
            )
        }
    }
}