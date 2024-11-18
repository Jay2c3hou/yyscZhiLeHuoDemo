package com.example.yyscdemo

import android.util.Log
import android.view.View

/***
 * 防止快速点击 , 这里用 Log.e 只是因为个人喜欢用 Log.e 来打印
 */
fun View.click(listener: (view: View) -> Unit) {
    val minTime = 500L
    var lastTime = 0L
    this.setOnClickListener {
        val tmpTime = System.currentTimeMillis()
        if (tmpTime - lastTime > minTime) {
            lastTime = tmpTime
            listener.invoke(this)
        } else {
            Log.e("yyscclick", "点击过快，取消触发")
        }
    }
}