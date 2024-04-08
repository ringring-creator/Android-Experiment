package com.ring.ring.login

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After

import org.junit.Before
import org.junit.Test

class RetrofitLoginDataSourceTest {
    private lateinit var subject: RetrofitLoginDataSource

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start()
        subject = RetrofitLoginDataSource(
            networkJson = Json {
                ignoreUnknownKeys = true
            },
            okhttpCallFactory = OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) },
                )
                .build(),
            baseUrl = mockWebServer.url("/").toString(),
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
        val actual = subject.login(LoginRequest(LoginRequest.Account("", "")))

        //then
        assertThat(actual.userId, equalTo(expectedUserId))
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
        subject.login(LoginRequest(LoginRequest.Account(email, password)))

        //then
        val request = mockWebServer.takeRequest()
        val body = request.body.readUtf8()
        assertThat(body.contains("\"email\":\"$email\""), `is`(true))
        assertThat(body.contains("\"password\":\"$password\""), `is`(true))
        assertThat(request.path, equalTo("/user/login"))
    }
}