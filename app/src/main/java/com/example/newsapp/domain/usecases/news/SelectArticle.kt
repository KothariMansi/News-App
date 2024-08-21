package com.example.newsapp.domain.usecases.news

import com.example.newsapp.data.local.NewsDao
import com.example.newsapp.domain.model.Article

class SelectArticle (
    private val newsDao: NewsDao
) {
    suspend operator fun invoke(url: String): Article? {
        return newsDao.getArticle(url)
    }
}