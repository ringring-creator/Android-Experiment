package com.ring.ring.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class DataStoreUserDataSourceTest {
    private lateinit var subject: DataStoreUserDataSource

    private val context: Context = ApplicationProvider.getApplicationContext()
    private lateinit var dataSource: DataStore<Preferences>

    private val dataStoreFileName = "user-settings-test"
    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
        dataStoreFileName
    )

    @Before
    fun setUp() {
        dataSource = context.userDataStore
        subject = DataStoreUserDataSource(
            dataStore = dataSource,
        )
    }

    @After
    fun tearDown() {
        context.dataStoreFile(dataStoreFileName).deleteOnExit()
    }

    @Test
    fun `get user data saved`() = runTest {
        //given
        val expected = User(
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
}