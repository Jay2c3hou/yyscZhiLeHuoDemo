package com.example.yyscdemo.demo3

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yyscdemo.databinding.ItemLessonTextBinding
import com.example.yyscdemo.model.SentenceXF

/**
 * @author : yysc
 * @date : 2024/11/23 16:54.
 * @description :
 */
class WordAdapter : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    private var highlightedPosition = -1
    private var words = listOf<SentenceXF>()

    // 设置高亮的字
    fun setHighlightedWord(position: Int) {
        highlightedPosition = position
        notifyItemChanged(position)  // 只刷新高亮的项
    }

    // 取消高亮
    fun setUnhighlightedWord(position: Int) {
        if (highlightedPosition == position) {
            highlightedPosition = -1
            notifyItemChanged(position)  // 只刷新取消高亮的项
        }
    }

    // 提交新的数据列表，并使用 DiffUtil 刷新
    fun submitList(newList: List<SentenceXF>) {
        val diffCallback = WordDiffCallback(words, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        words = newList
        diffResult.dispatchUpdatesTo(this)  // 只刷新变更的项
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

    class WordDiffCallback(
        private val oldList: List<SentenceXF>, private val newList: List<SentenceXF>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // 比较两个项是否是同一项
            return oldList[oldItemPosition].word == newList[newItemPosition].word
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // 比较两个项的内容是否相同
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    inner class WordViewHolder(private val binding: ItemLessonTextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SentenceXF, position: Int) {
            binding.tvRead.text = item.word
            if (position == highlightedPosition) {
                binding.tvRead.setTextColor(Color.RED) // 高亮显示
            } else {
                binding.tvRead.setTextColor(Color.BLACK) // 默认颜色
            }
        }
    }
}