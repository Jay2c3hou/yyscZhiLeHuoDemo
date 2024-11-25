package com.example.yyscdemo.demo2

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yyscdemo.model.Api
import com.example.yyscdemo.model.ContentData
import com.example.yyscdemo.model.SubjectData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * @author : yysc
 * @date : 2024/11/19 17:29.
 * @description :
 */
class SubjectViewModel(application: Application) : AndroidViewModel(application) {

    private val _subjectList: MutableSharedFlow<List<SubjectData>> = MutableSharedFlow()
    val subjectList = _subjectList.asSharedFlow()
    fun getSubjectList() {
        viewModelScope.launch(Dispatchers.IO) {
            flow<SubjectData> {
                val data = Api.api.getSubjectList().data
                Log.e("yyscdata", data.toString())
                _subjectList.emit(data)
            }.onStart {
                // 这里如果是按照mvi的思想的话 ,应该是控制视图的
            }.catch {
                Log.e("yysc", "getSubjectList: ${it.message}", it)
            }.collect()
        }
    }

    private val _difficulty = MutableStateFlow<List<Int>>(emptyList())
    val difficulty = _difficulty.asSharedFlow()
    fun getTopDifficulty() {
        viewModelScope.launch(Dispatchers.IO) {
            flow<Int> {
                val data = Api.api.getTopDifficulty().data
                Log.e("yyscdata", data.toString())
                _difficulty.emit(data)
            }.onStart {

            }.catch {
                Log.e("yysc", "getTopDifficulty: ${it.message}", it)
            }.collect()
        }
    }

    private val _content: MutableSharedFlow<ContentData> = MutableSharedFlow()
    val content = _content.asSharedFlow()
    fun getContent(
        lexile: Int, typeId: Int, page: Int, size: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            flow<ContentData> {
                val data = Api.api.getContent(lexile, typeId, page, size)
                Log.e("yyscgetContent", data.toString())
                _content.emit(data.data)
            }.onStart {}.catch {
                Log.e("yyscgetContent", "getContent: ${it.message}", it)
            }.collect()
        }
    }
}