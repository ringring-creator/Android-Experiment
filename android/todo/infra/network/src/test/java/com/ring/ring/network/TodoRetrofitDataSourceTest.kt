package com.ring.ring.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ring.ring.todo.infra.network.RetrofitTodoNetworkApi
import com.ring.ring.todo.infra.network.TodoRetrofitDataSource
import com.ring.ring.todo.infra.network.dto.CreateRequest
import com.ring.ring.todo.infra.network.dto.EditDoneRequest
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class TodoRetrofitDataSourceTest {
    private lateinit var subject: TodoRetrofitDataSource

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
            .create(RetrofitTodoNetworkApi::class.java)

        subject = TodoRetrofitDataSource(
            networkApi = networkApi,
        )
    }

    @Test
    fun `list parse json and fetch todos`() = runTest {
        //given
        val response = MockResponse()
            .setBody(""" { "todoList": [ { "id": 1, "title": "fakeTitle", "description": "fakeDescription", "done": false, "deadline": 1704067200000, "userId": 1 }, { "id": 2, "title": "fakeTitle2", "description": "fakeDescription2", "done": true, "deadline": 1735603200000, "userId": 1 } ] } """.trimIndent())
            .addHeader("Content-Type", "application/json")
            .setResponseCode(200)
        mockWebServer.enqueue(response)

        //when
        val actual = subject.list("fakeToken")

        //then
        val todoList = actual.todoList
        assertThat(todoList.count(), equalTo(2))
        val firstElement = todoList.first()
        assertThat(firstElement.id, equalTo(1))
        assertThat(firstElement.title, equalTo("fakeTitle"))
        assertThat(firstElement.description, equalTo("fakeDescription"))
        assertThat(firstElement.done, equalTo(false))
        assertThat(firstElement.deadline.toString(), equalTo("2024-01-01T00:00:00Z"))
        assertThat(firstElement.userId, equalTo(1L))
    }


    @Test
    fun `list request correct parameters`() = runTest {
        //given
        val response = MockResponse()
            .setBody(""" { "todoList": [ { "id": 1, "title": "fakeTitle", "description": "fakeDescription", "done": false, "deadline": 1704067200000, "userId": 1 }, { "id": 2, "title": "fakeTitle2", "description": "fakeDescription2", "done": true, "deadline": 1735603200000, "userId": 1 } ] } """.trimIndent())
            .addHeader("Content-Type", "application/json")
            .setResponseCode(200)
        mockWebServer.enqueue(response)

        //when
        val token = "fakeToken"
        subject.list(token)

        //then
        val request = mockWebServer.takeRequest()
        assertThat(request.getHeader("Authorization"), equalTo("Bearer $token"))
        assertThat(request.path, equalTo("/todo/list"))
    }

    @Test
    fun `create request correct parameters`() = runTest {
        //given
        val response = MockResponse()
            .setResponseCode(200)
        mockWebServer.enqueue(response)

        //when
        val token = "fakeToken"
        val title = "fakeTitle"
        val description = "fakeDescription"
        val done = false
        val deadline = Instant.parse("2024-01-01T00:00:00Z")
        subject.create(
            request = CreateRequest(
                title, description, done, deadline
            ), token
        )

        //then
        val request = mockWebServer.takeRequest()
        val body = request.body.readUtf8()
        assertThat(body.contains("\"title\":\"$title\""), `is`(true))
        assertThat(body.contains("\"description\":\"$description\""), `is`(true))
        assertThat(body.contains("\"done\":$done"), `is`(true))
        assertThat(
            body.contains("\"deadline\":${deadline.toEpochMilliseconds()}"),
            `is`(true)
        )
        assertThat(request.getHeader("Authorization"), equalTo("Bearer $token"))
        assertThat(request.path, equalTo("/todo/create"))
    }

    @Test
    fun `editDone request correct parameters`() = runTest {
        //given
        val response = MockResponse()
            .setResponseCode(200)
        mockWebServer.enqueue(response)

        //when
        val token = "fakeToken"
        val done = true
        val todoId = 1L
        subject.editDone(request = EditDoneRequest(todoId = todoId, done = done), token)

        //then
        val request = mockWebServer.takeRequest()
        val body = request.body.readUtf8()
        assertThat(body.contains("\"todoId\":$todoId"), `is`(true))
        assertThat(body.contains("\"done\":$done"), `is`(true))
        assertThat(request.getHeader("Authorization"), equalTo("Bearer $token"))
        assertThat(request.path, equalTo("/todo/edit-done"))
    }
}