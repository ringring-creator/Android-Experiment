package com.ring.ring.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ring.ring.user.infra.model.Credentials
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class UserRetrofitDataSourceTest {
    private lateinit var subject: UserRetrofitDataSource

    private val mockWebServer = MockWebServer()
    private val json = Json { ignoreUnknownKeys = true }
    private val okHttpClient = OkHttpClient.Builder().build()

    @Before
    fun setUp() {
        mockWebServer.start()
        val networkApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/").toString())
            .callFactory(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(RetrofitUserNetworkApi::class.java)

        subject = UserRetrofitDataSource(
            networkApi = networkApi,
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `login parse json and fetch user`() = runTest {
        //given
        val expectedUserId = 1L
        val expectedToken = "fakeToken"
        val response = MockResponse()
            .setBody(""" { "userId": $expectedUserId, "token": "$expectedToken" } """.trimIndent())
            .addHeader("Content-Type", "application/json")
            .setResponseCode(200)
        mockWebServer.enqueue(response)

        //when
        val actual = subject.login(Credentials("", ""))

        //then
        assertThat(actual.id, equalTo(expectedUserId))
        assertThat(actual.token, equalTo(expectedToken))
    }

    @Test
    fun `login request correct parameters`() = runTest {
        //given
        val response = MockResponse()
            .setBody(""" { "userId": ${1L}, "token": "${"fakeToken"}" } """.trimIndent())
            .addHeader("Content-Type", "application/json")
            .setResponseCode(200)
        mockWebServer.enqueue(response)

        //when
        val email = "fakeEmail"
        val password = "fakePassword"
        subject.login(Credentials(email, password))

        //then
        val request = mockWebServer.takeRequest()
        val body = request.body.readUtf8()
        assertThat(body.contains("\"email\":\"$email\""), `is`(true))
        assertThat(body.contains("\"password\":\"$password\""), `is`(true))
        assertThat(request.path, equalTo("/user/login"))
    }

    @Test
    fun `signUp request correct parameters`() = runTest {
        //given
        val response = MockResponse()
            .setResponseCode(200)
        mockWebServer.enqueue(response)

        //when
        val email = "fakeEmail"
        val password = "fakePassword"
        subject.signUp(Credentials(email, password))

        //then
        val request = mockWebServer.takeRequest()
        val body = request.body.readUtf8()
        assertThat(body.contains("\"email\":\"$email\""), `is`(true))
        assertThat(body.contains("\"password\":\"$password\""), `is`(true))
        assertThat(request.path, equalTo("/user/signup"))
    }
}