package com.ring.ring.user.feature.signup

import com.ring.ring.infra.test.MainDispatcherRule
import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.UserNetworkDataSource
import com.ring.ring.user.infra.test.FakeErrorUserNetworkDataSource
import com.ring.ring.user.infra.test.FakeUserNetworkDataSource
import io.mockk.coVerify
import io.mockk.mockk
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

    private var networkDataSource: UserNetworkDataSource = FakeUserNetworkDataSource()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    @Before
    fun setUp() {
        setupSubject()
    }

    @Test
    fun `setEmail when email is valid format`() {
        //given,when
        val value = "fake-email@gmail.com"
        val expect = SignUpUiState.Email(value, isError = false)
        subject.setEmail(value)

        //then
        assertThat(subject.uiState.value.email, equalTo(expect))
    }

    @Test
    fun `setEmail when email is invalid format`() {
        //given,when
        val value = "fake-email"
        val expect = SignUpUiState.Email(value, isError = true)
        subject.setEmail(value)

        //then
        assertThat(subject.uiState.value.email, equalTo(expect))
    }

    @Test
    fun `setPassword when password is valid`() {
        //given,when
        val value = "Fake-password123"
        subject.setPassword(value)

        //then
        val expected = SignUpUiState.Password(
            value = value,
            isError = false,
        )
        assertThat(subject.uiState.value.password, equalTo(expected))
    }

    @Test
    fun `setPassword set isError and visibleSupportText to true when password is less than 8 characters`() {
        //given,when
        val value = "fake-p"
        subject.setPassword(value)

        //then
        val expected = SignUpUiState.Password(
            value = value,
            isError = true,
        )
        assertThat(subject.uiState.value.password, equalTo(expected))
    }

    @Test
    fun `setPassword set isError and visibleSupportText to true when password has no number`() {
        //given,when
        val value = "fake-pFda"
        subject.setPassword(value)

        //then
        val expected = SignUpUiState.Password(
            value = value,
            isError = true,
        )
        assertThat(subject.uiState.value.password, equalTo(expected))
    }

    @Test
    fun `setPassword set isError and visibleSupportText to true when password has no uppercase`() {
        //given,when
        val value = "fake-pfda1"
        subject.setPassword(value)

        //then
        val expected = SignUpUiState.Password(
            value = value,
            isError = true,
        )
        assertThat(subject.uiState.value.password, equalTo(expected))
    }

    @Test
    fun `setPassword set isError and visibleSupportText to true when password has no lowercase`() {
        //given,when
        val value = "FAKE-PFDA1"
        subject.setPassword(value)

        //then
        val expected = SignUpUiState.Password(
            value = value,
            isError = true,
        )
        assertThat(subject.uiState.value.password, equalTo(expected))
    }

    @Test
    fun `signUp call signUp api`() = runTest {
        //given
        networkDataSource = mockk(relaxed = true)
        setupSubject()

        //when
        subject.signUp()
        advanceUntilIdle()

        //then
        coVerify {
            networkDataSource.signUp(
                Credentials(
                    subject.uiState.value.email.value,
                    subject.uiState.value.password.value,
                )
            )
        }
    }

    @Test
    fun `signUp send signUpFinishedEvent`() = runTest {
        //given
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.event.collect {
                if (it == SignUpEvent.SignUpSuccess) wasCalled = true
            }
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
        networkDataSource = FakeErrorUserNetworkDataSource()
        setupSubject()
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.event.collect {
                if (it == SignUpEvent.SignUpError) wasCalled = true
            }
        }

        //when
        subject.signUp()
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }

    private fun setupSubject() {
        subject = SignUpViewModel(
            userRepository = SignUpRepository(
                networkDataSource = networkDataSource,
            ),
        )
    }
}
