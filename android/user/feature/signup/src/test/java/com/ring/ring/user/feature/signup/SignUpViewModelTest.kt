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
    fun `setEmail set isError to true when email is invalid format`() {
        //given,when
        val value = "fake-email"
        subject.setEmail(value)

        //then
        assertThat(subject.uiState.value.email.value, equalTo(value))
        assertThat(subject.uiState.value.email.isError, `is`(true))
    }

    @Test
    fun `setPassword set isError to true when password is less than 8 characters`() {
        //given,when
        val value = "fake-p1"
        subject.setPassword(value)

        //then
        assertThat(subject.uiState.value.password.value, equalTo(value))
        assertThat(subject.uiState.value.password.isError, `is`(true))
    }

    @Test
    fun `setPassword set isError to true when password has no number`() {
        //given,when
        val value = "fake-pFda"
        subject.setPassword(value)

        //then
        assertThat(subject.uiState.value.password.value, equalTo(value))
        assertThat(subject.uiState.value.password.isError, `is`(true))
    }

    @Test
    fun `setPassword set isError to true when password has no uppercase`() {
        //given,when
        val value = "fake-pfda1"
        subject.setPassword(value)

        //then
        assertThat(subject.uiState.value.password.value, equalTo(value))
        assertThat(subject.uiState.value.password.isError, `is`(true))
    }

    @Test
    fun `setPassword set isError to true when password has no lowercase`() {
        //given,when
        val value = "FAKE-PFDA1"
        subject.setPassword(value)

        //then
        assertThat(subject.uiState.value.password.value, equalTo(value))
        assertThat(subject.uiState.value.password.isError, `is`(true))
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
