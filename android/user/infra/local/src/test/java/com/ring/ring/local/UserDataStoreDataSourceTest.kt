package com.ring.ring.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.test.core.app.ApplicationProvider
import com.ring.ring.test.fake.FakeLogger
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class UserDataStoreDataSourceTest {
    private lateinit var subject: UserDataStoreDataSource

    private val context: Context = ApplicationProvider.getApplicationContext()
    private lateinit var dataSource: DataStore<Preferences>
    private var logger: FakeLogger = FakeLogger()

    private val dataStoreFileName = "user-settings-test"
    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
        dataStoreFileName
    )

    @Before
    fun setUp() {
        dataSource = context.userDataStore
        setupSubject()
    }

    @After
    fun tearDown() {
        context.dataStoreFile(dataStoreFileName).deleteOnExit()
    }

    @Test
    fun `get user data saved`() = runTest {
        //given
        val expected = LocalUser(
            userId = 1L,
            email = "fakeEmail",
            token = "fakeToken",
        )
        subject.save(expected)
        advanceUntilIdle()

        //when
        val actual = subject.getUser()!!

        //then
        assertThat(actual.userId, equalTo(expected.userId))
        assertThat(actual.email, equalTo(expected.email))
        assertThat(actual.token, equalTo(expected.token))
    }

    @Test
    fun `get user write log when save failed`() = runTest {
        //given
        dataSource = mockk(relaxed = true)
        setupSubject()
        coEvery { dataSource.updateData(any()) } throws IOException()

        //when
        assertThrows(IOException::class.java) {
            runBlocking { subject.save(LocalUser(1L, "", "")) }
        }

        //then
        assertThat(logger.eWasCalled!!.tag, equalTo("DataStoreUserDataSource"))
        assertThat(logger.eWasCalled!!.msg, equalTo("failed to save"))
        assertThat(logger.eWasCalled!!.tr, instanceOf(IOException::class.java))
    }

    @Test
    fun `get user write log when getUser failed`() = runTest {
        //given
        dataSource = mockk(relaxed = true)
        setupSubject()
        coEvery { dataSource.data } throws IOException()

        //when
        assertThrows(IOException::class.java) {
            runBlocking { subject.getUser() }
        }

        //then
        assertThat(logger.eWasCalled!!.tag, equalTo("DataStoreUserDataSource"))
        assertThat(logger.eWasCalled!!.msg, equalTo("failed to getUser"))
        assertThat(logger.eWasCalled!!.tr, instanceOf(IOException::class.java))
    }

    private fun setupSubject() {
        subject = UserDataStoreDataSource(
            dataStore = dataSource,
            logger = logger,
        )
    }
}
