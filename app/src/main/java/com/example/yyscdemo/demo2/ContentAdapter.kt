package com.example.yyscdemo.demo2

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.yyscdemo.databinding.ItemLessonBinding
import com.example.yyscdemo.model.ContentItem

/**
 * @author : yysc
 * @date : 2024/11/22 17:13.
 * @description :
 */
class ContentAdapter : RecyclerView.Adapter<ContentAdapter.MyViewHolder>() {
    private val items = arrayListOf<ContentItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<ContentItem>, page: Int) {
        if (page == 1) items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val binding: ItemLessonBinding) : ViewHolder(binding.root) {
        fun bind(item: ContentItem) {
            binding.apply {
                Glide.with(binding.ivItemLesson).load(item.cover).into(binding.ivItemLesson)
                tvItemLessonTitle.text = item.title
                tvItemLessonDifficulty.text = String.format("难度：%s", item.lexile.toString())
                tvWordNum.text = String.format("%s词", item.wordNum.toString())
                tvItemLessonType.text = item.type
            }
        }
    }
}