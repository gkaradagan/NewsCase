package com.gorkem.news.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gorkem.news.BuildConfig
import com.gorkem.news.data.api.NewsService
import com.gorkem.news.data.model.Articles
import com.gorkem.news.data.model.ServiceResult
import com.gorkem.news.data.repository.NewsServiceRepository
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito

@RunWith(JUnit4::class)
class NewsServiceRepositoryTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: NewsServiceRepository

    private val service = Mockito.mock(NewsService::class.java)

    @Test
    fun testGetArticlesNotNullData() {
        runBlocking {
            repository = NewsServiceRepository(service)
            val articles = Articles(mutableListOf(), 0)
            Mockito.`when`(
                service.getArticles(
                    anyString(),
                    anyString(),
                    anyString()
                )
            ).thenReturn(articles)

            val request = repository.getArticles(
                anyString(),
                anyString(),
                anyString()
            )
            Assert.assertNotNull(request)
            Assert.assertNotNull(request.data)
        }
    }

    @Test
    fun testGetArticlesNullData() {
        repository = NewsServiceRepository(service)
        runBlocking {
            Mockito.`when`(
                service.getArticles(
                    anyString(),
                    anyString(),
                    anyString()
                )
            ).thenReturn(null)

            val request = repository.getArticles(
                anyString(),
                anyString(),
                anyString()
            )

            Assert.assertNotNull(request)//ServiceResult
            Assert.assertNull(request.data)
            Assert.assertThat(request.status, CoreMatchers.`is`(ServiceResult.Status.ERROR))

        }
    }

    @Test
    fun testGetArticlesStatusError() {
        runBlocking {
            repository = NewsServiceRepository(service)
            val articles = Articles(mutableListOf(), 0)
            articles.status = "error"
            Mockito.`when`(
                service.getArticles(
                    anyString(),
                    anyString(),
                    anyString()
                )
            ).thenReturn(articles)

            val request = repository.getArticles(
                anyString(),
                anyString(),
                anyString()
            )
            Assert.assertNotNull(request)//ServiceResult
            Assert.assertThat(request.status, CoreMatchers.`is`(ServiceResult.Status.ERROR))
        }
    }
}