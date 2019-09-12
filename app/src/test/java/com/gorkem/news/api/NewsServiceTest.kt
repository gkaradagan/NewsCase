package com.gorkem.news.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gorkem.news.BuildConfig
import com.gorkem.news.data.api.NewsService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class NewsServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: NewsService

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun requestForGetArticles() {
        runBlocking {
            enqueueResponse("newslist.json")
            val resultResponse = service.getArticles("futbool", "2019-09-12", "2019-09-12")

            val request = mockWebServer.takeRequest()
            Assert.assertNotNull(resultResponse)
            Assert.assertThat(request.path, CoreMatchers.`is`("/everything?sortBy=publishedAt&language=en&apikey=${BuildConfig.API_DEVELOPER_TOKEN}&q=futbool&from=2019-09-12&to=2019-09-12"))
        }
    }

    @Test
    fun checkGetArticlesResponse() {
        runBlocking {
            enqueueResponse("newslist.json")
            val resultResponse = service.getArticles("futbool", "2019-09-12", "2019-09-12")

            Assert.assertThat(resultResponse.totalResults, CoreMatchers.`is`(45394))
            Assert.assertThat(resultResponse.status, CoreMatchers.`is`("ok"))
        }
    }

    @Test
    fun checkGetArticlesResponseItem() {
        runBlocking {
            enqueueResponse("newslist.json")
            val resultResponse = service.getArticles("futbool", "2019-09-12", "2019-09-12")

            val article = resultResponse.articles[0]
            Assert.assertThat(article.title, CoreMatchers.`is`("Was a Child Spotted Smoking a Cigarette at a Soccer Game?"))
            Assert.assertThat(article.description, CoreMatchers.`is`("As we often say, appearances can be very deceiving."))
            Assert.assertThat(article.url, CoreMatchers.`is`("https://www.snopes.com/fact-check/child-smoking-cigarette-soccer/"))
            Assert.assertThat(article.urlToImage, CoreMatchers.`is`( "https://www.snopes.com/tachyon/2019/09/Young-boy-is-seen-smoking-by-TV-cameras-at-Turkish.jpg?fit=634,355"))
        }
    }


    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream("api-response/$fileName")
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse.setBody(
                source.readString(Charsets.UTF_8)
            )
        )
    }
}