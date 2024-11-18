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
 * @date : 2024/11/17 15:59.
 * @description :选择性别的那个 button
 */
class GenderSelectView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val tvGender: TextView
    private val icGender: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_select_gender_button, this, true)
        tvGender = findViewById(R.id.tv_gender)
        icGender = findViewById(R.id.ic_gender)

        context.obtainStyledAttributes(attrs, R.styleable.GenderSelectView).apply {
            tvGender.text = getString(R.styleable.TitleView_title)
            getDrawable(R.styleable.GenderSelectView_image)?.let { icGender.setBackgroundDrawable(it) }
            isSelected = getBoolean(R.styleable.GenderSelectView_isSelected, false)
            recycle()
        }
    }

}