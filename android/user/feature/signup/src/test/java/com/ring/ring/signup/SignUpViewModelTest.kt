package com.ring.ring.signup

import com.ring.ring.test.FakeUserNetworkDataSource
import com.ring.ring.test.MainDispatcherRule
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
class SignUpViewModelTest {
    private lateinit var subject: SignUpViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val networkDataSource = FakeUserNetworkDataSource()

    @Before
    fun setUp() {
        subject = SignUpViewModel(
            userRepository = SignUpUserRepository(
                networkDataSource = networkDataSource,
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
    fun `signUp call signUp api`() = runTest {
        subject.signUp()
        advanceUntilIdle()

        assertThat(networkDataSource.calledSignUpParameter!!.email, equalTo(subject.email.value))
        assertThat(
            networkDataSource.calledSignUpParameter!!.password,
            equalTo(subject.password.value)
        )
    }
}