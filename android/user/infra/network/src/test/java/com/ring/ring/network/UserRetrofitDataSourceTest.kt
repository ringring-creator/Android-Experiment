package com.ring.ring.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ring.ring.network.exception.ConflictException
import com.ring.ring.network.exception.UnauthorizedException
import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.Id
import kotlinx.coroutines.runBlocking
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
import org.junit.Assert
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
    fun `edit request correct parameters`() = runTest {
        //given
        val response = MockResponse()
            .setResponseCode(204)
        mockWebServer.enqueue(response)

        //when
        val credentials = Credentials.issue("email@example.com", "Abcdefg1")
        subject.edit(credentials)

        //then
        val request = mockWebServer.takeRequest()
        val body = request.body.readUtf8()
        assertThat(body.contains("\"email\":\"${credentials.email.value}\""), `is`(true))
        assertThat(body.contains("\"password\":\"${credentials.password.value}\""), `is`(true))
        assertThat(request.path, equalTo("/users"))
        assertThat(request.method, equalTo("PUT"))
    }

    @Test
    fun `edit throw UnauthorizedException when http status code is HTTP_UNAUTHORIZED`() {
        //given
        val response = MockResponse()
            .setBody(""" { "message": "Not logged in" } """.trimIndent())
            .addHeader("Content-Type", "application/json")
            .setResponseCode(401)
        mockWebServer.enqueue(response)

        //when,then
        Assert.assertThrows(UnauthorizedException::class.java) {
            runBlocking {
                subject.edit(Credentials.issue("email@example.com", "Abcdefg1"))
            }
        }
    }

    @Test
    fun `withdrawal request correct parameters`() = runTest {
        //given
        val response = MockResponse()
            .setResponseCode(204)
        mockWebServer.enqueue(response)

        //when
        subject.withdrawal()

        //then
        val request = mockWebServer.takeRequest()
        assertThat(request.path, equalTo("/users"))
        assertThat(request.method, equalTo("DELETE"))
    }

    @Test
    fun `withdrawal throw UnauthorizedException when http status code is HTTP_UNAUTHORIZED`() {
        //given
        val response = MockResponse()
            .setBody(""" { "message": "Not logged in" } """.trimIndent())
            .addHeader("Content-Type", "application/json")
            .setResponseCode(401)
        mockWebServer.enqueue(response)

        //when,then
        Assert.assertThrows(UnauthorizedException::class.java) {
            runBlocking {
                subject.withdrawal()
            }
        }
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
        val actual = subject.login(Credentials.issue("email@example.com", "Abcdefg1"))

        //then
        assertThat(actual.id, equalTo(Id(expectedUserId)))
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
        val email = "email@example.com"
        val password = "Abcdefg1"
        subject.login(Credentials.issue(email, password))

        //then
        val request = mockWebServer.takeRequest()
        val body = request.body.readUtf8()
        assertThat(body.contains("\"email\":\"$email\""), `is`(true))
        assertThat(body.contains("\"password\":\"$password\""), `is`(true))
        assertThat(request.path, equalTo("/login"))
    }

    @Test
    fun `signUp request correct parameters`() = runTest {
        //given
        val response = MockResponse()
            .setResponseCode(200)
        mockWebServer.enqueue(response)

        //when
        val email = "email@example.com"
        val password = "Abcdefg1"
        subject.signUp(Credentials.issue(email, password))

        //then
        val request = mockWebServer.takeRequest()
        val body = request.body.readUtf8()
        assertThat(body.contains("\"email\":\"$email\""), `is`(true))
        assertThat(body.contains("\"password\":\"$password\""), `is`(true))
        assertThat(request.path, equalTo("/signup"))
    }

    @Test
    fun `signUp throw ConflictException when http status code is HTTP_CONFLICT`() {
        //given
        val response = MockResponse()
            .setBody(""" { "message": "Email is already registered" } """.trimIndent())
            .addHeader("Content-Type", "application/json")
            .setResponseCode(409)
        mockWebServer.enqueue(response)

        //when,then
        Assert.assertThrows(ConflictException::class.java) {
            runBlocking {
                subject.signUp(Credentials.issue("email@example.com", "Abcdefg1"))

            }
        }
    }
}