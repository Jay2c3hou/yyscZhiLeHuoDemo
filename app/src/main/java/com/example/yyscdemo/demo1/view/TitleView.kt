package com.example.yyscdemo.demo1.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.yyscdemo.R

/**
 * @author : yysc
 * @date : 2024/11/17 14:21.
 * @description :标题栏
 */
class TitleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val titleTextView: TextView
    private val icFinish: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_title, this, true)
        titleTextView = findViewById(R.id.tv_title)
        icFinish = findViewById(R.id.ic_finish)

        context.obtainStyledAttributes(attrs, R.styleable.TitleView).apply {
            titleTextView.text = this.getString(R.styleable.TitleView_title)
            recycle()
        }
    }

    fun getLeftArrow() = icFinish
}