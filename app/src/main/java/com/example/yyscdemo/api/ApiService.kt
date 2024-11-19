package com.example.yyscdemo.api

import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.GET

/**
 * @author : yysc
 * @date : 2024/11/19 17:01.
 * @description :
 */
interface ApiService {
    @GET("library/articleTypeList")
    suspend fun getSubjectList(): SubjectData

    @GET("appArticle/selectList")
    suspend fun getTopDifficulty() {

    }

    @GET("library/articleList")
    suspend fun getContent(@Body request: ArticleRequest) {

    }
}