package com.example.yyscdemo.demo2

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.yyscdemo.databinding.ItemLeftListBinding
import com.example.yyscdemo.model.SubjectData

/**
 * @author : yysc
 * @date : 2024/11/20 22:19.
 * @description :
 */
class SubjectAdapter(
    private val items: List<SubjectData>, private val onItemSelectedListener: OnItemSelectedListener
) : RecyclerView.Adapter<SubjectAdapter.MyViewHolder>() {

    private var selectedPosition = -1

    interface OnItemSelectedListener {
        fun onItemSelected(position: Int, typeId: Int)
        fun onItemUnselected(position: Int)
    }

    // 更新选中项的方法
    fun setSelectedPosition(position: Int) {
        val previousSelectedPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousSelectedPosition)  // 刷新上一个选中的项
        notifyItemChanged(selectedPosition)          // 刷新当前选中的项
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemLeftListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = items.size

    inner class MyViewHolder(private val binding: ItemLeftListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = items[position]
            binding.tvSubject.text = item.type
            binding.ivFeaturedRead.isVisible = item.isFeatured
            // 设置背景色
            if (position == selectedPosition) {
                binding.root.setBackgroundColor(Color.WHITE)  // 选中项背景为白色
                binding.tvSubject.setTextColor(Color.parseColor("#67AEFC"))
                binding.vSubjectSelected.isVisible = true
            } else {
                binding.root.setBackgroundColor(Color.parseColor("#F0F0F5"))  // 非选中项背景为 #F0F0F5
                binding.tvSubject.setTextColor(Color.parseColor("#5E5E60"))
                binding.vSubjectSelected.isVisible = false
            }

            binding.root.setOnClickListener {
                val previousSelectedPosition = selectedPosition
                if (selectedPosition != position) {
                    onItemSelectedListener.onItemSelected(position, item.id) // 通知选中
                    selectedPosition = position
                }
                notifyItemChanged(previousSelectedPosition) // 更新之前选中的项
                notifyItemChanged(selectedPosition) // 更新当前选中的项
            }
        }
    }
}