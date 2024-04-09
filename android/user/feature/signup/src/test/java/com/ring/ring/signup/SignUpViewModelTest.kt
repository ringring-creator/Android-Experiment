package com.ring.ring.signup

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
    fun `signUp call signUp api`() = runTest {
        //given,when
        subject.signUp()
        advanceUntilIdle()

        //then
        assertThat(networkDataSource.calledSignUpParameter!!.email, equalTo(subject.email.value))
        assertThat(
            networkDataSource.calledSignUpParameter!!.password,
            equalTo(subject.password.value)
        )
    }

    @Test
    fun `signUp send signUpFinishedEvent`() = runTest {
        //given
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.signUpFinishedEvent.collect { wasCalled = true }
        }

        //when
        subject.signUp()
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }
}