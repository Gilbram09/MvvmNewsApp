package com.gilbram.mvvmnewsapp.network

import com.gilbram.mvvmnewsapp.model.NewsResponse
import com.gilbram.mvvmnewsapp.util.Constans.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "id",

        @Query("page")
        pageNumber: Int = 1,

        @Query("apiKey")
        apkCode: String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q")
        searQuery : String ,

        @Query("page")
        pageNumber : Int = 1,

        @Query("apiKey")
        apkCode : String = API_KEY
    ): Response<NewsResponse>
}