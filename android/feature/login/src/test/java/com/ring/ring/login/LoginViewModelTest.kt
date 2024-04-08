package com.ring.ring.login

import com.ring.ring.login.fake.FakeLoginNetworkDataSource
import com.ring.ring.login.fake.FakeUserLocalDataSource
import com.ring.ring.network.LoginRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private lateinit var subject: LoginViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val networkDataSource = FakeLoginNetworkDataSource()
    private val localDataSource = FakeUserLocalDataSource()

    @Before
    fun setUp() {
        subject = LoginViewModel(
            userRepository = DefaultUserRepository(
                networkDataSource = networkDataSource,
                localDataSource = localDataSource,
            )
        )
    }

    @Test
    fun setEmail() {
        val expect = "fake-email"
        subject.setEmail(expect)


        assertThat(subject.email.value, equalTo(expect))
    }

    @Test
    fun setPassword() {
        val expect = "fake-password"
        subject.setPassword(expect)


        assertThat(subject.password.value, equalTo(expect))
    }

    @Test
    fun `login save user from networkDataSource`() = runTest {
        val expectedEmail = "fakeEmail"
        subject.setEmail(expectedEmail)
        subject.login()
        advanceUntilIdle()


        val expect = networkDataSource.login(
            LoginRequest(
                LoginRequest.Account(
                    "dummy",
                    "dummy"
                )
            )
        )


        val actual = localDataSource.getUser()
        assertThat(actual.userId, equalTo(expect.userId))
        assertThat(actual.email, equalTo(expectedEmail))
        assertThat(actual.token, equalTo(expect.token))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login send loginFinishedEvent`() = runTest {
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.loginFinishedEvent.collect { wasCalled = true }
        }


        subject.login()
        advanceUntilIdle()


        assertThat(wasCalled, `is`(true))
    }
}