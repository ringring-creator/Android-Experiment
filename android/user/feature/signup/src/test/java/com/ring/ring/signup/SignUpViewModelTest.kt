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

    private var networkDataSource = FakeUserNetworkDataSource()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    @Before
    fun setUp() {
        setupSignUpViewModel()
    }

    @Test
    fun `setEmail when email is valid format`() {
        //given,when
        val value = "fake-email@gmail.com"
        val expect = SignUpUiState.Email(value, isError = false, visibleSupportingText = false)
        subject.setEmail(value)

        //then
        assertThat(subject.email.value, equalTo(expect))
    }

    @Test
    fun `setEmail when email is invalid format`() {
        //given,when
        val value = "fake-email"
        val expect = SignUpUiState.Email(value, isError = true, visibleSupportingText = true)
        subject.setEmail(value)

        //then
        assertThat(subject.email.value, equalTo(expect))
    }

    @Test
    fun `setPassword when password is valid`() {
        //given,when
        val value = "Fake-password123"
        subject.setPassword(value)

        //then
        val expected = SignUpUiState.Password(
            input = value,
            isError = false,
            visibleSupportingText = false,
        )
        assertThat(subject.password.value, equalTo(expected))
    }

    @Test
    fun `setPassword set isError and visibleSupportText to true when password is less than 8 characters`() {
        //given,when
        val value = "fake-p"
        subject.setPassword(value)

        //then
        val expected = SignUpUiState.Password(
            input = value,
            isError = true,
            visibleSupportingText = true,
        )
        assertThat(subject.password.value, equalTo(expected))
    }

    @Test
    fun `setPassword set isError and visibleSupportText to true when password has no number`() {
        //given,when
        val value = "fake-pFda"
        subject.setPassword(value)

        //then
        val expected = SignUpUiState.Password(
            input = value,
            isError = true,
            visibleSupportingText = true,
        )
        assertThat(subject.password.value, equalTo(expected))
    }

    @Test
    fun `setPassword set isError and visibleSupportText to true when password has no uppercase`() {
        //given,when
        val value = "fake-pfda1"
        subject.setPassword(value)

        //then
        val expected = SignUpUiState.Password(
            input = value,
            isError = true,
            visibleSupportingText = true,
        )
        assertThat(subject.password.value, equalTo(expected))
    }

    @Test
    fun `setPassword set isError and visibleSupportText to true when password has no lowercase`() {
        //given,when
        val value = "FAKE-PFDA1"
        subject.setPassword(value)

        //then
        val expected = SignUpUiState.Password(
            input = value,
            isError = true,
            visibleSupportingText = true,
        )
        assertThat(subject.password.value, equalTo(expected))
    }

    @Test
    fun `signUp call signUp api`() = runTest {
        //given,when
        subject.signUp()
        advanceUntilIdle()

        //then
        assertThat(
            networkDataSource.calledSignUpParameter!!.email,
            equalTo(subject.email.value.input)
        )
        assertThat(
            networkDataSource.calledSignUpParameter!!.password,
            equalTo(subject.password.value.input)
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

    @Test
    fun `signUp send signUpFailedEvent when sign up failed`() = runTest {
        //given
        networkDataSource = FakeUserNetworkDataSource(isSimulateError = true)
        setupSignUpViewModel()
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.signUpFailedEvent.collect { wasCalled = true }
        }

        //when
        subject.signUp()
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }

    private fun setupSignUpViewModel() {
        subject = SignUpViewModel(
            userRepository = SignUpRepository(
                networkDataSource = networkDataSource,
            ),
        )
    }
}
