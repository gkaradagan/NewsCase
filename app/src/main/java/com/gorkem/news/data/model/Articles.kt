package com.gorkem.news.data.model

open class Response {
    var status: String = ""
    var message: String = ""
}

data class Articles(
    val articles: List<Article>,
    val totalResults: Int
) : Response()

data class Article(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
)

data class Source(
    val id: String,
    val name: String
)