package com.example.yyscdemo.model

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * @author : yysc
 * @date : 2024/11/19 17:01.
 * @description :
 */
interface ApiService {
    /**
     * demo 2的左侧学科内容
     */
    @GET("englishgpt/library/articleTypeList")
    suspend fun getSubjectList(): BaseDataResponse<List<SubjectData>>

    /**
     * demo 2的顶部难度值
     */
    @GET("englishgpt/appArticle/selectList")
    suspend fun getTopDifficulty(): BaseDataResponse<List<Int>>

    /**
     * demo2 的内容
     */
    @Headers("Cookie: sid=OFvEbpyl4PyKkc/cSjl2tW3g5Ga/z5DPSQRGQn8mJBs=")
    @GET("englishgpt/library/articleList")
    suspend fun getContent(
        @Query("lexile") lexile: Int,
        @Query("typeId") typeId: Int,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
    ): BaseDataResponse<ContentData>

    /**
     * deomo 3 读一读数据
     */
    @Headers("Cookie: sid=i5VMMK2c7EEm5qK597kJeDqrel7NKCRqSQRGQn8mJBs=")
    @GET("knowledge/article/getArticleDetail")
    suspend fun getReadList(
        @Query("aid") aid: Int,
    ): BaseDataResponse<LessonData>
}