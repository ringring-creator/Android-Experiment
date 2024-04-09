package com.ring.ring.login

import com.ring.ring.network.LoginRequest
import com.ring.ring.test.MainDispatcherRule
import com.ring.ring.test.fake.FakeLoginNetworkDataSource
import com.ring.ring.test.fake.FakeUserLocalDataSource
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
            userRepository = LoginUserRepository(
                networkDataSource = networkDataSource,
                localDataSource = localDataSource,
            )
        )
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
}