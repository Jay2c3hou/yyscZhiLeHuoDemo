package com.example.yyscdemo.demo3

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yyscdemo.databinding.ItemLessonTextBinding
import com.example.yyscdemo.model.SentenceXF

/**
 * @author : yysc
 * @date : 2024/11/23 16:54.
 * @description :
 */
class WordAdapter(private val words: List<SentenceXF>) :
    RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    private var highlightedPosition = -1

    // 设置高亮的字
    fun setHighlightedWord(position: Int) {
        highlightedPosition = position
        notifyDataSetChanged()
    }

    // 取消高亮
    fun setUnhighlightedWord(position: Int) {
        if (highlightedPosition == position) {
            highlightedPosition = -1
            notifyDataSetChanged()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val textView =
            ItemLessonTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(textView)
    }

    override fun getItemCount(): Int {
        return words.size
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(words[position], position)
    }

    inner class WordViewHolder(private val binding: ItemLessonTextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SentenceXF, position: Int) {

//            if (item.needSpace) {
//                binding.tvRead.text = String.format(" %s", item.word)
//            } else {
            binding.tvRead.text = item.word
//            }
            if (position == highlightedPosition) {
                binding.tvRead.setTextColor(Color.RED) // 高亮显示
            } else {
                binding.tvRead.setTextColor(Color.BLACK) // 默认颜色
            }

        }
    }
}