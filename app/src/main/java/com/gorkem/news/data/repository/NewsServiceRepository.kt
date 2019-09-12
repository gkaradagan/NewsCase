package com.gorkem.news.data.repository

import com.gorkem.news.base.BaseRepository
import com.gorkem.news.data.api.NewsService
import com.gorkem.news.data.model.Articles
import com.gorkem.news.data.model.ServiceResult
import javax.inject.Inject

class NewsServiceRepository @Inject constructor(private var service: NewsService) :
    BaseRepository() {

    suspend fun getArticles(
        articleType: String,
        from: String?,
        to: String?
    ): ServiceResult<Articles> {
        return call { service.getArticles(articleType, from, to) }
    }
}