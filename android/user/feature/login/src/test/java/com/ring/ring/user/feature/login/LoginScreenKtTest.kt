package com.ring.ring.user.feature.login

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.test.FakeUserLocalDataSource
import com.ring.ring.user.infra.test.FakeUserNetworkDataSource
import com.ring.ring.user.infra.test.TestActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
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
class LoginScreenKtTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    private val savedCredentials = Credentials.issue("email@example.com", "Abcdefg1")
    private val networkDataSource = FakeUserNetworkDataSource(mutableListOf(savedCredentials))
    private val localDataSource = FakeUserLocalDataSource()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `tapped login button call toTodoListScreen`() {
        //given
        var wasCalled = false
        setupLoginScreen(toTodoListScreen = { wasCalled = true })
        composeTestRule
            .onNodeWithTag("EmailTextField")
            .performTextInput(savedCredentials.email.value)
        composeTestRule
            .onNodeWithTag("PasswordTextField")
            .performTextInput(savedCredentials.password.value)

        //when
        composeTestRule
            .onNodeWithTag("LoginButton")
            .performClick()
        composeTestRule.waitForIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun `tapped login button show snackbar when login failed`() {
        //given
        setupLoginScreen()

        //when
        composeTestRule
            .onNodeWithTag("LoginButton")
            .performClick()

        //then
        composeTestRule.onNodeWithText("Failed to login").assertExists()
    }

    private fun setupLoginScreen(
        toTodoListScreen: () -> Unit = {},
        toSignUpScreen: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            LoginScreen(
                toTodoListScreen = toTodoListScreen,
                toSignUpScreen = toSignUpScreen,
                viewModel = LoginViewModel(
                    userRepository = LoginRepository(
                        networkDataSource = networkDataSource,
                        localDataSource = localDataSource,
                    )
                )
            )
        }
    }
}
