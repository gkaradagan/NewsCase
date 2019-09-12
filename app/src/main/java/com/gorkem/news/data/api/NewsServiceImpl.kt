package com.gorkem.news.data.api

import com.gorkem.news.data.model.Articles
import retrofit2.Retrofit
import javax.inject.Inject

open class NewsServiceImpl constructor(
    retrofit: Retrofit
) : NewsService {

    private var service: NewsService = retrofit.create(NewsService::class.java)

    override suspend fun getArticles(articleType: String, from: String?, to: String?): Articles {
        return service.getArticles(articleType, from, to)
    }


}