package com.example.newsapp.presentation.details

import com.example.newsapp.domain.model.Article

sealed class DetailsEvents {
    data class UpsertDeleteArticle(val article: Article): DetailsEvents()
    object RemoveSideEffect: DetailsEvents()
}