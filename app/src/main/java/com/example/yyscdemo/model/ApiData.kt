package com.example.yyscdemo.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author : yysc
 * @date : 2024/11/19 17:22.
 * @description : demo 中所需的数据
 */

data class BaseDataResponse<T>(
    val code: Int, val msg: String, val data: T
)

/**
 * demo 2 顶部难度值
 */
data class ArticleRequest(
    val lexile: Int?, val typeId: Int?, val page: Int = 1, val size: Int = 10
)

/**
 * demo 2 左侧学科内容
 */
data class SubjectData(
    val id: Int, val type: String, val isFeatured: Boolean = false
)

data class ContentData(
    val total: Int,
    val list: List<ContentItem>,
    val pageNum: Int,
    val pageSize: Int,
    val size: Int,
    val startRow: Int,
    val endRow: Int,
    val pages: Int,
    val prePage: Int,
    val nextPage: Int,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean,
    val navigatePages: Int,
    val navigatepageNums: List<Int>,
    val navigateFirstPage: Int,
    val navigateLastPage: Int,
    val lastPage: Int,
    val firstPage: Int
)

data class ContentItem(
    val id: Int,
    val title: String,
    val wordNum: Int,
    val lexile: Int,
    val typeId: Int,
    val type: String,
    val cover: String,
    val clickRatio: Any?, // 可以是 null
    val accuracy: Any?, // 可以是 null
    val accuracyRatio: Any?, // 可以是 null
    val color: Any?, // 可以是 null
    val isRead: Int,
    val readTime: Any?, // 可以是 null
    val stage: String? // 可以是 null
)


/**
 * demo3 课文数据
 */
data class LessonData(
    @SerializedName("id") val id: Int,// 文章id
    @SerializedName("cover") val cover: String,// 封面
    @SerializedName("title") val title: String,// 标题
    @SerializedName("wordNum") val wordNum: Int,// 单词数
    @SerializedName("readCount") val readCount: Int,// 阅读次数
    @SerializedName("bgmUrl") val bgmUrl: String,// 内容背景音乐
    @SerializedName("typeId") val typeId: Int,// 主题id
    @SerializedName("typeName") val typeName: String,// 主题名
    @SerializedName("level") val level: Int,// 通过的关卡
    @SerializedName("talkItContent") val talkItContent: String,// 说一说内容
    @SerializedName("talkItAudio") val talkItAudio: String,// 说一说音频
    @SerializedName("imgList") val imgList: List<String>,// 图片集合
    @SerializedName("readReportId") val readReportId: Int = -1,// 阅读报告id，-1表示没有
    @SerializedName("readId") val readId: Int = -1,// 阅读详情id，-1表示没有阅读过
    @SerializedName("contentList") val contentList: ArrayList<ContentList>,// 内容列表
)

@Parcelize
data class ContentList(
    @SerializedName("pageNum") val pageNum: Int,// 页码
    @SerializedName("imgUrl") val imgUrl: String,// 插图链接
    @SerializedName("audioUrl") val audioUrl: String,// 音频链接
    @SerializedName("sentence") val sentence: String,// 句子内容
    @SerializedName("sentenceByXFList") val sentenceByXFList: List<SentenceXF>,// 讯飞分割好的句子
) : Parcelable

@Parcelize
data class SentenceXF(
    @SerializedName("word") val word: String,// 字
    @SerializedName("wb") val wb: Int,// 开始帧 10 毫秒
    @SerializedName("we") val we: Int,// 结束帧 10 毫秒
//    var needSpace: Boolean = false// 需不需要空格
) : Parcelable
