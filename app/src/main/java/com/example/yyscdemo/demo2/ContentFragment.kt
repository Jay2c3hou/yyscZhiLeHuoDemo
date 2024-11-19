package com.example.yyscdemo.demo2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.yyscdemo.databinding.FragmentContentBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * @author : yysc
 * @date : 2024/11/19 17:53.
 * @description :文库页 带有课程 item 的vp
 */
private const val ARG_LEXILE = "arg_lexile"

class ContentFragment : Fragment() {
    private lateinit var _binding: FragmentContentBinding
    private val binding get() = _binding
    private var typeId: Int? = null
    private var lexile: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lexile = it.getInt(ARG_LEXILE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initUI()
    }

    private fun initUI() {
        binding.vpContent.adapter = MyPagerAdapter(requireActivity())
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        TabLayoutMediator(binding.tabLayout, binding.vpContent) { tab, position ->
            // 应该是要设置customView 的,这里简单一点就好(主要是懒的弄图片了)

        }.attach()
    }

    private fun initData() {

    }

    inner class MyPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int {
            // TODO: 等服务好了 tab.size() 难度值
            return 0
        }

        override fun createFragment(position: Int): Fragment {
            return LessonFragment.newInstance(lexile!!, typeId!!)
        }
    }

    companion object {
        fun newInstance(lexile: Int): ContentFragment {
            val fragment = ContentFragment().apply {
                arguments?.apply {
                    putInt(ARG_LEXILE, lexile)
                }
            }
            return fragment
        }
    }
}