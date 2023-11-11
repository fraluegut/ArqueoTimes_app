package com.arqus.arqueotimes3.network

import com.arqus.arqueotimes3.model.Article
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("wp-json/wp/v2/posts") // Reemplaza "articles" con la ruta real de la API
    fun getArticles(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<List<Article>>

}
