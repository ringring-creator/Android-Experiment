@file:OptIn(ExperimentalCoroutinesApi::class)

package com.ring.ring.user.feature.mypage.viewmodel

import com.ring.ring.infra.test.MainDispatcherRule
import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.UserNetworkDataSource
import com.ring.ring.user.infra.test.FakeUserLocalDataSource
import com.ring.ring.user.infra.test.FakeUserNetworkDataSource
import com.ring.ring.user.infra.test.userTestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertThrows
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(MyPageViewModelTest.MainTest::class, MyPageViewModelTest.SetPasswordTest::class)
class MyPageViewModelTest {
    class MainTest {
        private lateinit var subject: MyPageViewModel

        @get:Rule
        val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

        private var networkDataSource: UserNetworkDataSource = FakeUserNetworkDataSource(
            user = userTestData,
            credentials = Credentials.issue(userTestData.email.value, userTestData.password.value)
        )
        private var localDataSource = FakeUserLocalDataSource(userTestData)

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
        fun `edit call edit api`() = runTest {
            //given
            setupSubject()

            //when
            subject.setEmail("email2@example.com")
            subject.setPassword("Abcdefg2")
            subject.edit()
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

        @Test
        fun `edit send UnauthorizedErrorEvent when get user failed`() = runTest {
            //given
            localDataSource = FakeUserLocalDataSource(user = null)
            setupSubject()
            var wasCalled = false
            TestScope(UnconfinedTestDispatcher()).launch {
                subject.event.collect {
                    if (it == MyPageEvent.UnauthorizedError) wasCalled = true
                }
            }

            //when
            subject.setEmail("email2@example.com")
            subject.setPassword("Abcdefg2")
            subject.edit()
            advanceUntilIdle()

            //then
            assertThat(wasCalled, `is`(true))
        }

        @Test
        fun `withdrawal call withdrawal api`() = runTest {
            //given
            setupSubject()

            //when
            subject.withdrawal()
            advanceUntilIdle()

            //then
            assertThrows(Exception::class.java) {
                runBlocking {
                    networkDataSource.login(
                        Credentials.issue(
                            userTestData.email.value,
                            userTestData.password.value,
                        )
                    )
                }
            }
        }

        @Test
        fun `withdrawal delete user in local`() = runTest {
            //given
            setupSubject()

            //when
            subject.withdrawal()
            advanceUntilIdle()

            //then
            val actual = localDataSource.getUser()
            assertThat(actual, nullValue())
        }

        @Test
        fun `withdrawal send UnauthorizedErrorEvent when get user failed`() = runTest {
            //given
            localDataSource = FakeUserLocalDataSource(user = null)
            setupSubject()
            var wasCalled = false
            TestScope(UnconfinedTestDispatcher()).launch {
                subject.event.collect {
                    if (it == MyPageEvent.UnauthorizedError) wasCalled = true
                }
            }

            //when
            subject.withdrawal()
            advanceUntilIdle()

            //then
            assertThat(wasCalled, `is`(true))
        }

        private fun setupSubject() {
            subject = MyPageViewModel(
                repository = MyPageRepository(
                    networkDataSource = networkDataSource,
                    localDataSource = localDataSource,
                ),
            )
        }
    }

    @RunWith(Parameterized::class)
    class SetPasswordTest(
        private val name: String,
        private val value: String,
        private val expected: Boolean,
    ) {
        @Test
        fun setPassword() {
            //given
            val subject = MyPageViewModel(
                repository = MyPageRepository(
                    networkDataSource = FakeUserNetworkDataSource(),
                    localDataSource = FakeUserLocalDataSource(),
                ),
            )

            //when
            subject.setPassword(value)

            //then
            assertThat(subject.uiState.value.password.value, equalTo(value))
            assertThat(subject.uiState.value.password.isError, `is`(expected))
        }

        companion object {
            @Parameterized.Parameters(name = "{0}: value={1}, expected={2}")
            @JvmStatic
            fun data() = listOf(
                arrayOf("password is secure", "Abcdefg1", false),
                arrayOf("password is less than 8 characters", "fake-p1", true),
                arrayOf("password has no number", "fake-pFda", true),
                arrayOf("password has no uppercase", "fake-pfda1", true),
                arrayOf("password has no lowercase", "FAKE-PFDA1", true),
            )
        }
    }
}