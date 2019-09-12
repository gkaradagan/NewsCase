package com.gorkem.news.data.api

import com.gorkem.news.BuildConfig
import com.gorkem.news.data.model.Articles
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    companion object {
        const val ENDPOINT = "https://newsapi.org/v2/"
    }

    @GET("everything?sortBy=publishedAt&language=en&apikey=${BuildConfig.API_DEVELOPER_TOKEN}")
    suspend fun getArticles(
        @Query("q") articleType: String,
        @Query("from") from: String?,
        @Query("to") to: String?
    ): Articles
}