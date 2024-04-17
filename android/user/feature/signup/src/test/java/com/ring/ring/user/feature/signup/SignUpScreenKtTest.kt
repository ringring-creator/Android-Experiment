package com.ring.ring.user.feature.signup

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.UserNetworkDataSource
import com.ring.ring.user.infra.test.FakeErrorUserNetworkDataSource
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
class SignUpScreenKtTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    private val savedCredentials = Credentials.issue("email@example.com", "Abcdefg1")
    private var networkDataSource: UserNetworkDataSource =
        FakeUserNetworkDataSource(mutableListOf(savedCredentials))

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `tapped sign up button navigate to login screen`() {
        //given
        var wasCalled = false
        setupSignUpScreen { wasCalled = true }
        composeTestRule
            .onNodeWithTag("EmailTextField")
            .performTextInput(savedCredentials.email.value)
        composeTestRule
            .onNodeWithTag("PasswordTextField")
            .performTextInput(savedCredentials.password.value)

        //when
        composeTestRule
            .onNodeWithTag("SignUpButton")
            .performClick()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun `tapped sign up button show snackbar when sign up failed`() {
        //given
        networkDataSource = FakeErrorUserNetworkDataSource()
        setupSignUpScreen()

        //when
        composeTestRule
            .onNodeWithTag("SignUpButton")
            .performClick()

        //then
        composeTestRule.onNodeWithText("Failed to sign up").assertExists()
    }

    private fun setupSignUpScreen(toLoginScreen: () -> Unit = {}) {
        composeTestRule.setContent {
            SignUpScreen(
                toLoginScreen = toLoginScreen,
                viewModel = SignUpViewModel(
                    userRepository = SignUpRepository(
                        networkDataSource = networkDataSource
                    )
                )
            )
        }
    }
}