package com.example.yyscdemo.demo2

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yyscdemo.api.Api
import com.example.yyscdemo.api.ApiService
import com.example.yyscdemo.api.SubjectData
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author : yysc
 * @date : 2024/11/19 17:29.
 * @description :
 */
class SubjectViewModel(application: Application) : AndroidViewModel(application) {

    private val _subjectList: MutableSharedFlow<SubjectData> = MutableSharedFlow()
    val subjectList = _subjectList.asSharedFlow()

    suspend fun getSubjectList() {
        viewModelScope.launch(Dispatchers.IO) {
            flow<SubjectData> {
                val data = Api.api.getSubjectList()
                _subjectList.emit(data)
            }.onStart {
                // 这里如果是按照mvi的思想的话 ,应该是控制试图的
            }.catch {
                Log.e("yysc", "getSubjectList: ${it.message}", it)
            }.collect()
        }
    }
}