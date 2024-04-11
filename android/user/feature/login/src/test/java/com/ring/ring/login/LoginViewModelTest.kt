package com.ring.ring.login

import com.ring.ring.network.LoginRequest
import com.ring.ring.test.FakeUserLocalDataSource
import com.ring.ring.test.FakeUserNetworkDataSource
import com.ring.ring.test.MainDispatcherRule
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

    private var networkDataSource = FakeUserNetworkDataSource()
    private var localDataSource = FakeUserLocalDataSource()

    @Before
    fun setUp() {
        setupSubject()
    }

    @Test
    fun setEmail() {
        //given,when
        val expect = "fake-email"
        subject.setEmail(expect)

        //then
        assertThat(subject.email.value, equalTo(expect))
    }

    @Test
    fun setPassword() {
        //given,when
        val expect = "fake-password"
        subject.setPassword(expect)

        //then
        assertThat(subject.password.value, equalTo(expect))
    }

    @Test
    fun `login save user from networkDataSource`() = runTest {
        //given
        val expectedEmail = "fakeEmail"
        subject.setEmail(expectedEmail)
        subject.login()
        advanceUntilIdle()

        //when
        val expect = networkDataSource.login(
            LoginRequest(
                LoginRequest.Credentials(
                    "dummy",
                    "dummy"
                )
            )
        )

        //then
        val actual = localDataSource.getUser()
        assertThat(actual.userId, equalTo(expect.userId))
        assertThat(actual.email, equalTo(expectedEmail))
        assertThat(actual.token, equalTo(expect.token))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login send loginFinishedEvent`() = runTest {
        //given
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.loginFinishedEvent.collect { wasCalled = true }
        }

        //when
        subject.login()
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login send loginFailedEvent when login failed`() = runTest {
        //given
        networkDataSource = FakeUserNetworkDataSource(isSimulateError = true)
        setupSubject()
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.loginFailedEvent.collect { wasCalled = true }
        }

        //when
        subject.login()
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }

    private fun setupSubject() {
        subject = LoginViewModel(
            userRepository = LoginRepository(
                networkDataSource = networkDataSource,
                localDataSource = localDataSource,
            )
        )
    }
}