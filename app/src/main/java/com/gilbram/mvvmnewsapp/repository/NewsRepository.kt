package com.gilbram.mvvmnewsapp.repository

import com.gilbram.mvvmnewsapp.database.ArticleDatabase
import com.gilbram.mvvmnewsapp.model.Article
import com.gilbram.mvvmnewsapp.network.RetrofitInstance

class NewsRepository(val db : ArticleDatabase){
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int)=
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery: String,pageNumber: Int)=
        RetrofitInstance.api.searchNews(searchQuery,pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticles(article)
}