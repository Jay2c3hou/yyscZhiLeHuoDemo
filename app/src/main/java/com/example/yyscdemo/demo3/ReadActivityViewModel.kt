package com.example.yyscdemo.demo3

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yyscdemo.model.Api
import com.example.yyscdemo.model.LessonData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author : yysc
 * @date : 2024/11/20 14:52.
 * @description :
 */
class ReadActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val _lessonList = MutableStateFlow<LessonData?>(null)
    val lessonList = _lessonList.asStateFlow()

    fun getReadList(aid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = Api.api.getReadList(aid).data
                if (data != null) {
                    _lessonList.tryEmit(data)
                    Log.e("yyscgetReadList", "数据: $data")
                } else {
                    Log.e("yyscgetReadList", "返回数据为空")
                }
            } catch (e: Exception) {
                Log.e("yyscgetReadList", "getReadList: ${e.message}", e)
            }
        }
    }
}