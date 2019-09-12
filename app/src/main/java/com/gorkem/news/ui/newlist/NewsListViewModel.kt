package com.gorkem.news.ui.newlist

import com.gorkem.news.base.BaseViewModel
import com.gorkem.news.data.repository.NewsServiceRepository
import javax.inject.Inject

class NewsListViewModel @Inject constructor(var newsRepository: NewsServiceRepository) : BaseViewModel() {

    fun getArticles(articleType: String = "football", from: String? = null, to: String? = null) =
        call { newsRepository.getArticles(articleType,from,to) }
}