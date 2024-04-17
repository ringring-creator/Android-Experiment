package com.ring.ring.user.feature.login

import com.ring.ring.infra.test.MainDispatcherRule
import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.Email
import com.ring.ring.user.infra.model.UserNetworkDataSource
import com.ring.ring.user.infra.test.FakeUserLocalDataSource
import com.ring.ring.user.infra.test.FakeUserNetworkDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private lateinit var subject: LoginViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val savedCredentials = Credentials.issue("email@example.com", "Abcdefg1")
    private var networkDataSource: UserNetworkDataSource = FakeUserNetworkDataSource(
        credentialsList = mutableListOf(savedCredentials),
    )
    private var localDataSource = FakeUserLocalDataSource()

    @Before
    fun setUp() {
        setupSubject()
    }

    @Test
    fun `login save user from networkDataSource`() = runTest {
        //given
        subject.setEmail(savedCredentials.email.value)
        subject.setPassword(savedCredentials.password.value)

        //when
        subject.login()
        advanceUntilIdle()

        //then
        val expect = networkDataSource.login(
            credentials = Credentials.issue(
                savedCredentials.email.value,
                savedCredentials.password.value
            )
        )
        val actual = localDataSource.getUser()
        assertThat(actual.id, equalTo(expect.id))
        assertThat(actual.email, equalTo(Email(savedCredentials.email.value)))
        assertThat(actual.token, equalTo(expect.token))
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