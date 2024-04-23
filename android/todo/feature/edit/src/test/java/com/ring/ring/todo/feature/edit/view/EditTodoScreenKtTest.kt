package com.ring.ring.todo.feature.edit.view

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.SavedStateHandle
import com.ring.ring.todo.feature.edit.EditTodoNav
import com.ring.ring.todo.feature.edit.viewmodel.EditTodoRepository
import com.ring.ring.todo.feature.edit.viewmodel.EditTodoViewModel
import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.todo.infra.network.exception.UnauthorizedException
import com.ring.ring.todo.infra.test.FakeTodoNetworkDataSource
import com.ring.ring.user.infra.test.FakeUserLocalDataSource
import com.ring.ring.user.infra.test.TestActivity
import com.ring.ring.user.infra.test.userTestData
import com.ring.ring.util.date.DefaultDateUtil
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
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
class EditTodoScreenKtTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    private val todo = Todo(
        1L,
        "fakeTitle",
        "fakeDescription",
        true,
        Instant.parse("2024-01-01T00:00:00Z"),
    )

    private var networkDataSource: TodoNetworkDataSource = FakeTodoNetworkDataSource(
        userTestData.token, values = mutableListOf(todo)
    )
    private var userLocalDataSource = FakeUserLocalDataSource(userTestData)
    private var snackbarHostState = SnackbarHostState()
    private lateinit var savedStateHandle: SavedStateHandle
    private val dateUtil = DefaultDateUtil()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `set todo fetched from network`() {
        //given,when
        setupEditTodoScreen()

        //then
        composeTestRule
            .onNodeWithText(todo.title)
            .assertExists()
        composeTestRule
            .onNodeWithText(todo.description)
            .assertExists()
        composeTestRule
            .onNodeWithTag("DoneCheckBox")
            .assertIsOn()
        composeTestRule
            .onNodeWithText(dateUtil.format(todo.deadline))
            .assertExists()
    }

    @Test
    fun `show snackbar when get todo failed`() {
        //given,when
        networkDataSource = mockk(relaxed = true) {
            coEvery { fetch(any(), any()) } throws Exception()
        }
        setupEditTodoScreen()

        //then
        composeTestRule
            .onNodeWithText("Failed to get todo")
            .assertExists()
    }

    @Test
    fun `get todo toLoginScreen when not authenticated`() {
        //given
        networkDataSource = mockk<TodoNetworkDataSource>(relaxed = true) {
            coEvery { fetch(any(), any()) } throws UnauthorizedException()
        }
        var wasCalled = false
        setupEditTodoScreen(toLoginScreen = { wasCalled = true })

        //when
        snackbarHostState.currentSnackbarData?.performAction()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun `tapped editButton save todo`() {
        //given
        setupEditTodoScreen()
        runBlocking { networkDataSource.create(todo, userTestData.token) }
        val titleTextField = composeTestRule.onNodeWithTag("TitleTextField")
        titleTextField.performTextClearance()
        titleTextField.performTextInput("title")
        val descriptionTextField = composeTestRule.onNodeWithTag("DescriptionTextField")
        descriptionTextField.performTextClearance()
        descriptionTextField.performTextInput("description")
        composeTestRule
            .onNodeWithTag("DoneCheckBox")
            .performClick()

        //when
        composeTestRule
            .onNodeWithTag("EditButton")
            .performClick()

        //then
        runBlocking {
            val actual = networkDataSource.fetchList(userTestData.token).find { it.id == todo.id }!!
            assertThat(actual.title, equalTo("title"))
            assertThat(actual.description, equalTo("description"))
            assertThat(actual.done, `is`(false))
        }
    }

    @Test
    fun `tapped editButton show snackbar`() {
        //given
        setupEditTodoScreen()
        runBlocking { networkDataSource.create(todo, userTestData.token) }

        //when
        composeTestRule
            .onNodeWithTag("EditButton")
            .performClick()

        //then
        composeTestRule
            .onNodeWithText("Success to edit")
            .assertExists()
    }

    @Test
    fun `tapped editButton show snackbar when edit failed`() {
        //given
        networkDataSource = mockk(relaxed = true) {
            coEvery { update(any(), any()) } throws Exception()
        }
        setupEditTodoScreen()
        snackbarHostState.currentSnackbarData?.dismiss()

        //when
        composeTestRule
            .onNodeWithTag("EditButton")
            .performClick()

        //then
        composeTestRule
            .onNodeWithText("Failed to edit")
            .assertExists()
    }

    @Test
    fun `tapped editButton toLoginScreen when unauthorized`() {
        //given
        networkDataSource = mockk<TodoNetworkDataSource>(relaxed = true) {
            coEvery { fetch(any(), any()) } returns todo
            coEvery { update(any(), any()) } throws UnauthorizedException()
        }
        var wasCalled = false
        setupEditTodoScreen(toLoginScreen = { wasCalled = true })

        //when
        composeTestRule
            .onNodeWithTag("EditButton")
            .performClick()
        snackbarHostState.currentSnackbarData?.performAction()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun `tapped deleteButton delete todo`() {
        //given
        setupEditTodoScreen()
        runBlocking { networkDataSource.create(todo, userTestData.token) }

        //when
        composeTestRule
            .onNodeWithTag("DeleteButton")
            .performClick()

        //then
        runBlocking {
            val actual = networkDataSource.fetchList(userTestData.token).find { it.id == todo.id }
            assertThat(actual, nullValue())
        }
    }

    @Test
    fun `tapped deleteButton toLoginScreen when unauthorized`() {
        networkDataSource = mockk<TodoNetworkDataSource>(relaxed = true) {
            coEvery { fetch(any(), any()) } returns todo
            coEvery { delete(any(), any()) } throws UnauthorizedException()
        }
        var wasCalled = false
        setupEditTodoScreen(toLoginScreen = { wasCalled = true })

        //when
        composeTestRule
            .onNodeWithTag("DeleteButton")
            .performClick()
        snackbarHostState.currentSnackbarData?.performAction()

        //then
        assertThat(wasCalled, `is`(true))
    }

    private fun setupEditTodoScreen(
        toTodoListScreen: () -> Unit = {},
        toLoginScreen: () -> Unit = {},
    ) {
        savedStateHandle = SavedStateHandle(mapOf(EditTodoNav.ID to todo.id))
        composeTestRule.setContent {
            EditTodoScreen(
                viewModel = EditTodoViewModel(
                    todoRepository = EditTodoRepository(
                        networkDataSource = networkDataSource,
                        userLocalDataSource = userLocalDataSource,
                        dateUtil = dateUtil
                    ),
                    dateUtil = dateUtil,
                    savedStateHandle = savedStateHandle,
                ),
                toTodoListScreen = toTodoListScreen,
                toLoginScreen = toLoginScreen,
                snackbarHostState = snackbarHostState,
            )
        }
    }
}