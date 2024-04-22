package com.ring.ring.user.feature.mypage

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.ring.ring.network.exception.UnauthorizedException
import com.ring.ring.user.infra.model.UserLocalDataSource
import com.ring.ring.user.infra.model.UserNetworkDataSource
import com.ring.ring.user.infra.test.FakeErrorUserNetworkDataSource
import com.ring.ring.user.infra.test.FakeUserLocalDataSource
import com.ring.ring.user.infra.test.FakeUserNetworkDataSource
import com.ring.ring.user.infra.test.TestActivity
import com.ring.ring.user.infra.test.userTestData
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.coEvery
import io.mockk.mockk
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
class MyPageScreenKtTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    private var networkDataSource: UserNetworkDataSource = FakeUserNetworkDataSource(
        user = userTestData
    )
    private var localDataSource: UserLocalDataSource = FakeUserLocalDataSource(userTestData)
    private val snackbarHostState = SnackbarHostState()


    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `set user fetched from local`() {
        //given,when
        setupMyPageScreen()

        //then
        composeTestRule
            .onNodeWithText(userTestData.email.value)
            .assertExists()
    }

    @Test
    fun `tapped logout dropdown item transit login screen`() {
        //given
        var wasCalled = false
        setupMyPageScreen(toLoginScreen = { wasCalled = true })

        //when
        composeTestRule
            .onNodeWithTag("ActionMenuIcon")
            .performClick()
        composeTestRule
            .onNodeWithTag("LogoutDropdownMenu")
            .performClick()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun `tapped logout dropdown item transit login screen when logout failed`() {
        //given
        localDataSource = mockk<UserLocalDataSource>(relaxed = true) {
            coEvery { delete() } throws Exception()
        }
        var wasCalled = false
        setupMyPageScreen(toLoginScreen = { wasCalled = true })

        //when
        composeTestRule
            .onNodeWithTag("ActionMenuIcon")
            .performClick()
        composeTestRule
            .onNodeWithTag("LogoutDropdownMenu")
            .performClick()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun `tapped edit button show snackbar`() {
        //given
        setupMyPageScreen()
        composeTestRule
            .onNodeWithTag("PasswordTextField")
            .performTextInput("Abcdefg2")

        //when
        composeTestRule
            .onNodeWithTag("EditButton")
            .performClick()

        //then
        composeTestRule.onNodeWithText("Success to edit").assertExists()
    }

    @Test
    fun `tapped edit button show error snackbar when edit failed`() {
        //given
        networkDataSource = FakeErrorUserNetworkDataSource()
        setupMyPageScreen()
        composeTestRule
            .onNodeWithTag("PasswordTextField")
            .performTextInput("Abcdefg2")

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
    fun `tapped edit button toLoginScreen when unauthorized`() {
        //given
        networkDataSource = mockk<UserNetworkDataSource>(relaxed = true) {
            coEvery { edit(any(), any()) } throws UnauthorizedException()
        }
        var wasCalled = false
        setupMyPageScreen(toLoginScreen = { wasCalled = true })
        composeTestRule
            .onNodeWithTag("PasswordTextField")
            .performTextInput("Abcdefg2")

        //when
        composeTestRule
            .onNodeWithTag("EditButton")
            .performClick()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun `tapped withdrawal button transit login screen`() {
        //given
        var wasCalled = false
        setupMyPageScreen(toLoginScreen = {
            wasCalled = true
        })

        //when
        composeTestRule
            .onNodeWithTag("WithdrawalButton")
            .performClick()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun `tapped withdrawal button show snackbar when withdrawal failed`() {
        //given
        networkDataSource = FakeErrorUserNetworkDataSource()
        setupMyPageScreen()

        //when
        composeTestRule
            .onNodeWithTag("WithdrawalButton")
            .performClick()

        //then
        composeTestRule
            .onNodeWithText("Failed to withdrawal")
            .assertExists()
    }

    @Test
    fun `tapped withdrawal button toLoginScreen when unauthorized`() {
        //given
        networkDataSource = mockk<UserNetworkDataSource>(relaxed = true) {
            coEvery { withdrawal(any()) } throws UnauthorizedException()
        }
        var wasCalled = false
        setupMyPageScreen(toLoginScreen = { wasCalled = true })

        //when
        composeTestRule
            .onNodeWithTag("WithdrawalButton")
            .performClick()

        //then
        assertThat(wasCalled, `is`(true))
    }

    private fun setupMyPageScreen(
        toLoginScreen: () -> Unit = {},
        toTodoListScreen: () -> Unit = {},
        toMyPageScreen: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            MyPageScreen(
                viewModel = MyPageViewModel(
                    repository = MyPageRepository(
                        networkDataSource = networkDataSource,
                        localDataSource = localDataSource,
                    ),
                ),
                toLoginScreen = toLoginScreen,
                toTodoListScreen = toTodoListScreen,
                toMyPageScreen = toMyPageScreen,
                snackbarHostState = snackbarHostState
            )
        }
    }
}