package com.ring.ring.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.network.RetrofitTodoNetworkApi
import com.ring.ring.todo.infra.network.TodoRetrofitDataSource
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
        val actual = subject.fetchList("fakeToken")

        //then
        assertThat(actual.count(), equalTo(2))
        val firstElement = actual.first()
        assertThat(firstElement.id, equalTo(1))
        assertThat(firstElement.title, equalTo("fakeTitle"))
        assertThat(firstElement.description, equalTo("fakeDescription"))
        assertThat(firstElement.done, equalTo(false))
        assertThat(firstElement.deadline.toString(), equalTo("2024-01-01T00:00:00Z"))
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
        subject.fetchList(token)

        //then
        val request = mockWebServer.takeRequest()
        assertThat(request.getHeader("Authorization"), equalTo("Bearer $token"))
        assertThat(request.path, equalTo("/todos"))
        assertThat(request.method, equalTo("GET"))
    }

    @Test
    fun `get request correct parameters`() = runTest {
        //given
        val response = MockResponse()
            .setBody(""" { "todo": {"id": 1, "title": "fakeTitle", "description": "fakeDescription", "done": false, "deadline": 1704067200000 } } """.trimIndent())
            .setResponseCode(200)
        mockWebServer.enqueue(response)

        //when
        val id = 1L
        val token = "fakeToken"
        subject.fetch(todoId = id, token = token)

        //then
        val request = mockWebServer.takeRequest()
        assertThat(request.getHeader("Authorization"), equalTo("Bearer $token"))
        assertThat(request.path, equalTo("/todos/$id"))
        assertThat(request.method, equalTo("GET"))
    }

    @Test
    fun `get parse json and fetch todos`() = runTest {
        //given
        val response = MockResponse()
            .setBody(""" { "todo": {"id": 1, "title": "fakeTitle", "description": "fakeDescription", "done": false, "deadline": 1704067200000 } } """.trimIndent())
            .setResponseCode(200)
        mockWebServer.enqueue(response)

        //when
        val id = 1L
        val actual = subject.fetch(todoId = id, token = "fakeToken")

        //then
        assertThat(actual.id, equalTo(id))
        assertThat(actual.title, equalTo("fakeTitle"))
        assertThat(actual.description, equalTo("fakeDescription"))
        assertThat(actual.done, equalTo(false))
        assertThat(actual.deadline.toString(), equalTo("2024-01-01T00:00:00Z"))
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
            todo = Todo(null, title, description, done, deadline),
            token = token,
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
        assertThat(request.path, equalTo("/todos"))
        assertThat(request.method, equalTo("POST"))
    }

    @Test
    fun `edit request correct parameters`() = runTest {
        //given
        val response = MockResponse()
            .setResponseCode(200)
        mockWebServer.enqueue(response)

        //when
        val id = 1L
        val title = "fakeTitle"
        val description = "fakeDescription"
        val done = false
        val deadline = Instant.parse("2024-01-01T00:00:00Z")
        val token = "fakeToken"
        subject.update(
            todo = Todo(id, title, description, done, deadline),
            token = token,
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
        assertThat(request.path, equalTo("/todos/${id}"))
        assertThat(request.method, equalTo("PUT"))
    }

    @Test
    fun `delete request correct parameters`() = runTest {
        //given
        val response = MockResponse()
            .setResponseCode(200)
        mockWebServer.enqueue(response)

        //when
        val id = 1L
        val token = "fakeToken"
        subject.delete(
            todoId = id,
            token = token,
        )

        //then
        val request = mockWebServer.takeRequest()
        assertThat(request.getHeader("Authorization"), equalTo("Bearer $token"))
        assertThat(request.path, equalTo("/todos/$id"))
        assertThat(request.method, equalTo("DELETE"))
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
        subject.updateDone(todoId = todoId, done = done, token)

        //then
        val request = mockWebServer.takeRequest()
        val body = request.body.readUtf8()
        assertThat(body.contains("\"done\":$done"), `is`(true))
        assertThat(request.getHeader("Authorization"), equalTo("Bearer $token"))
        assertThat(request.path, equalTo("/todos/edit-done/${todoId}"))
        assertThat(request.method, equalTo("PATCH"))
    }
}