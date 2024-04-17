package com.ring.ring.user.feature.signup

import com.ring.ring.infra.test.MainDispatcherRule
import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.UserNetworkDataSource
import com.ring.ring.user.infra.test.FakeUserNetworkDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Suite


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(Suite::class)
@Suite.SuiteClasses(SignUpViewModelTest.MainTest::class, SignUpViewModelTest.SetPasswordTest::class)
class SignUpViewModelTest {
    class MainTest {
        private lateinit var subject: SignUpViewModel

        private var networkDataSource: UserNetworkDataSource = FakeUserNetworkDataSource()

        @get:Rule
        val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

        @Before
        fun setUp() {
            setupSubject()
        }

        @Test
        fun `setEmail set isError to true when email is invalid format`() {
            //given,when
            val value = "fake-email"
            subject.setEmail(value)

            //then
            assertThat(subject.uiState.value.email.value, equalTo(value))
            assertThat(subject.uiState.value.email.isError, `is`(true))
        }

        @Test
        fun `signUp call signUp api`() = runTest {
            //given
            setupSubject()

            //when
            subject.setEmail("email@example.com")
            subject.setPassword("Abcdefg1")
            subject.signUp()
            advanceUntilIdle()

            //then
            try {
                networkDataSource.login(
                    Credentials.issue(
                        subject.uiState.value.email.value,
                        subject.uiState.value.password.value,
                    )
                )
            } catch (e: Throwable) {
                fail("Exception was thrown")
            }
        }

        private fun setupSubject() {
            subject = SignUpViewModel(
                userRepository = SignUpRepository(
                    networkDataSource = networkDataSource,
                ),
            )
        }
    }

    @RunWith(Parameterized::class)
    class SetPasswordTest(
        private val name: String,
        private val value: String,
        private val expected: Boolean,
    ) {
        @Test
        fun setPassword() {
            //given
            val subject = SignUpViewModel(
                userRepository = SignUpRepository(
                    networkDataSource = FakeUserNetworkDataSource(),
                ),
            )

            //when
            subject.setPassword(value)

            //then
            assertThat(subject.uiState.value.password.value, equalTo(value))
            assertThat(subject.uiState.value.password.isError, `is`(expected))
        }

        companion object {
            @Parameterized.Parameters(name = "{0}: value={1}, expected={2}")
            @JvmStatic
            fun data() = listOf(
                arrayOf("password is secure", "Abcdefg1", false),
                arrayOf("password is less than 8 characters", "fake-p1", true),
                arrayOf("password has no number", "fake-pFda", true),
                arrayOf("password has no uppercase", "fake-pfda1", true),
                arrayOf("password has no lowercase", "FAKE-PFDA1", true),
            )
        }
    }
}