package com.example.yyscdemo.demo2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.yyscdemo.api.SubjectData
import com.example.yyscdemo.databinding.FragmentSubjectBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

/**
 * @author : yysc
 * @date : 2024/11/19 15:50.
 * @description : 文库页 结合了左侧学科类型的vp
 */
class SubjectFragment : Fragment() {

    private lateinit var _binding: FragmentSubjectBinding
    private val binding get() = _binding
    private val viewModel by activityViewModels<SubjectViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSubjectBinding.inflate(inflater, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        //网络请求（这里是下拉刷新）
//        mBindingView.smartRefresh.setOnRefreshListener {
//            lifecycleScope.launch {
//                viewModel.searchUser()
//
//            }
//        }
        lifecycleScope.launch {
            viewModel.getSubjectList()
        }

        fun setViewVisible(visible: Boolean) {
            binding.leftTab.isVisible = visible
            binding.vpSubject.isVisible = visible
        }

        lifecycleScope.launch {
            setViewVisible(false)
            viewModel.subjectList.collect {
                setViewVisible(true)
                binding.vpSubject.adapter = MyPagerAdapter(requireActivity(), it.data.size)
                binding.leftTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }
                })
                TabLayoutMediator(binding.leftTab, binding.vpSubject) { tab, position ->
                    // 应该是要设置customView 的,这里简单一点就好(主要是懒的弄图片了)
                    tab.text = it.data[position]
                }.attach()
            }
        }
    }

    inner class MyPagerAdapter(fragmentActivity: FragmentActivity, private val tabCount: Int) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int {
            return tabCount
        }

        override fun createFragment(position: Int): Fragment {
            return ContentFragment.newInstance(position)
        }
    }
}