package com.gorkem.news.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gorkem.news.data.api.NewsService
import com.gorkem.news.data.model.Articles
import com.gorkem.news.data.model.ServiceResult
import com.gorkem.news.data.repository.NewsServiceRepository
import com.gorkem.news.ui.newlist.NewsListViewModel
import com.gorkem.news.util.CoroutinesTestRule
import com.gorkem.news.util.LiveDataTestUtil
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito

@RunWith(JUnit4::class)
class NewsListViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private lateinit var repository: NewsServiceRepository
    private val service = Mockito.mock(NewsService::class.java)
    private lateinit var viewModel: NewsListViewModel

    @Test
    fun testGetArticlesNotNullData() = coroutinesTestRule.testDispatcher.runBlockingTest {
        repository = NewsServiceRepository(service)
        viewModel = NewsListViewModel(repository)
        runBlocking {
            val articles = Articles(mutableListOf(), 0)
            Mockito.`when`(
                service.getArticles(
                    anyString(),
                    anyString(),
                    anyString()
                )
            ).thenReturn(articles)

            val request = viewModel.getArticles(
                anyString(),
                anyString(),
                anyString()
            )

            Assert.assertNotNull(request)
            Assert.assertNotNull(LiveDataTestUtil.getValue(request))
            Assert.assertThat(LiveDataTestUtil.getValue(request).status, CoreMatchers.`is`(ServiceResult.Status.SUCCESS))
        }
    }

    @Test
    fun testGetArticlesNullData() = coroutinesTestRule.testDispatcher.runBlockingTest {
        repository = NewsServiceRepository(service)
        viewModel = NewsListViewModel(repository)
        runBlocking {
            Mockito.`when`(
                service.getArticles(
                    anyString(),
                    anyString(),
                    anyString()
                )
            ).thenReturn(null)

            val request = viewModel.getArticles(
                anyString(),
                anyString(),
                anyString()
            )

            Assert.assertNotNull(request)
            val value = LiveDataTestUtil.getValue(request)
            Assert.assertNotNull(value)
            Assert.assertThat(LiveDataTestUtil.getValue(request).status, CoreMatchers.`is`(ServiceResult.Status.ERROR))
        }
    }

}