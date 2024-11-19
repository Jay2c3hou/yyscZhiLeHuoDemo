package com.example.yyscdemo.api

/**
 * @author : yysc
 * @date : 2024/11/19 17:22.
 * @description :
 */
data class ArticleRequest(
    val lexile: Int?, val typeId: Int?, val page: Int = 1, val size: Int = 10
)

data class SubjectData(
    val data: List<String>
)
