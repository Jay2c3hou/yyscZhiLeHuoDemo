package com.example.yyscdemo.demo2

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yyscdemo.databinding.ItemDifficultyBinding
import com.example.yyscdemo.demo2.SubjectAdapter.OnItemSelectedListener

/**
 * @author : yysc
 * @date : 2024/11/22 11:36.
 * @description :
 */
class DifficultyAdapter(
    private val items: List<Int>, private val onItemSelectedListener: OnItemSelectedListener
) : RecyclerView.Adapter<DifficultyAdapter.MyViewHolder>() {

    interface OnItemSelectedListener {
        fun onItemSelected(position: Int, difficulty: Int)
        fun onItemUnselected(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemDifficultyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    private var selectedPosition = 0

    inner class MyViewHolder(private val binding: ItemDifficultyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Int, position: Int) {
            binding.apply {
                tvDifficulty.text = item.toString()
                if (position == selectedPosition) {
                    root.isSelected = true
                    binding.tvDifficulty.setTextColor(Color.parseColor("#67AEFC"))
                    onItemSelectedListener.onItemSelected(position, item)
                } else {
                    root.isSelected = false
                    binding.tvDifficulty.setTextColor(Color.parseColor("#5E5E60"))
                    onItemSelectedListener.onItemUnselected(position)
                }

                root.setOnClickListener {
                    if (selectedPosition == position) return@setOnClickListener
                    val previousSelectedPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(position)// 更新当前点击的项
                    notifyItemChanged(previousSelectedPosition)
                }
            }
        }
    }
}