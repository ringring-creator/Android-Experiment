package com.ring.ring.user.feature.login

import com.ring.ring.infra.test.MainDispatcherRule
import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.UserNetworkDataSource
import com.ring.ring.user.infra.test.FakeErrorUserNetworkDataSource
import com.ring.ring.user.infra.test.FakeUserLocalDataSource
import com.ring.ring.user.infra.test.FakeUserNetworkDataSource
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

    private var networkDataSource: UserNetworkDataSource = FakeUserNetworkDataSource()
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
        val password = "fakePassword"
        networkDataSource.signUp(Credentials(expectedEmail, password))
        subject.setEmail(expectedEmail)
        subject.setPassword(password)
        subject.login()
        advanceUntilIdle()

        //when
        val expect = networkDataSource.login(Credentials(expectedEmail, password))

        //then
        val actual = localDataSource.getUser()
        assertThat(actual.id, equalTo(expect.id))
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
        subject.setEmail("defaultEmail")
        subject.setPassword("defaultPassword")
        subject.login()
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login send loginFailedEvent when login failed`() = runTest {
        //given
        networkDataSource = FakeErrorUserNetworkDataSource()
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