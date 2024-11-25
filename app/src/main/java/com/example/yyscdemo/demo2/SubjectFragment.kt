package com.example.yyscdemo.demo2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.yyscdemo.databinding.FragmentSubjectBinding
import com.example.yyscdemo.model.SubjectData
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * @author : yysc
 * @date : 2024/11/19 15:50.
 * @description : 文库页 结合了左侧学科类型的vp
 * 需要注意的是 因为接口没有给 课文内容右下角的"type"的颜色,所以这里就还没做 ,我猜这个没给颜色的话,应该是客户端本地做,就弄个表,id为key,color为value就行
 *
 */
class SubjectFragment : Fragment() {

    private var _binding: FragmentSubjectBinding? = null
    private val binding get() = requireNotNull(_binding) { "The property of binding has been destroyed." }
    private val viewModel by activityViewModels<SubjectViewModel>()
    private var page = 1 //当前请求的页数，默认1
    private var size = 10 //后端默认为10条
    private var lexile = 5 //难度值（从右侧顶部难度值接口选择的值）,这个的默认值应该是用户设置的一个值,但是我这个demo得不到,就随便给了一个5
    private var typeId = 0 //分类id  --推荐阅读时传0或null，其余情况传左边列表接口的id
    private val adapter = ContentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initUI()
    }

    private fun initUI() {
        val context = context ?: return
        binding.refreshLayout.apply {
            setRefreshHeader(ClassicsHeader(context))
            setRefreshFooter(ClassicsFooter(context))
            setOnRefreshListener {
                page = 1
                getContent(true) // 传递 true 表示刷新操作
            }
            setOnLoadMoreListener {
                page++
                getContent(false)
            }
        }
        binding.rvContent.adapter = adapter
        viewModel.getSubjectList()
        viewModel.getTopDifficulty()
    }

    private fun observeData() {

        // 设置视图的可见性
        fun setViewVisible(visible: Boolean) {
            binding.apply {
                pbSubject.isVisible = !visible
                leftTab.isVisible = visible
                tvDifficulty.isVisible = visible
                rvDifficulty.isVisible = visible
            }
        }

        // 提取初始化适配器的函数
        fun setUpAdapters(subjects: List<SubjectData>, difficulty: List<Int>) {
            // 设置左侧标签的适配器
            binding.leftTab.adapter =
                SubjectAdapter(subjects, object : SubjectAdapter.OnItemSelectedListener {
                    override fun onItemSelected(position: Int, typeId: Int) {
                        Log.e("yysc", "leftTab 选中了$typeId")
                        page = 1
                        this@SubjectFragment.typeId = typeId
                        getContent()
                    }

                    override fun onItemUnselected(position: Int) {}
                }).apply {
                    setSelectedPosition(0) // 默认选中第一个
                }

            // 设置难度的适配器
            binding.rvDifficulty.adapter =
                DifficultyAdapter(difficulty, object : DifficultyAdapter.OnItemSelectedListener {
                    override fun onItemSelected(position: Int, difficulty: Int) {
                        Log.e("yysc", "difficulty 选中了 $difficulty")
                        this@SubjectFragment.lexile = difficulty
                        getContent()
                    }

                    override fun onItemUnselected(position: Int) {}
                })
        }

        // 使用 combine 等待两个 flow 都 emit 数据
        viewModel.subjectList.combine(viewModel.difficulty) { subjects, difficulty ->
            // 将两个流的结果合并为一个 Pair
            Pair(subjects, difficulty)
        }.onEach { (subjects, difficulty) ->
            // 处理数据更新逻辑
            val updatedList = listOf(SubjectData(0, "精选阅读", true)) + subjects
            setUpAdapters(updatedList, difficulty)
            setViewVisible(true)  // 数据加载完成，更新视图可见性
        }.onCompletion {
            // 在流完成后执行的操作
            Log.e("yysc", "combine 流完成，开始执行后续任务")
            viewModel.getContent(lexile, typeId, 0, size)
        }.launchIn(lifecycleScope)

        // 监听 content 数据的变化
        lifecycleScope.launch {
            viewModel.content.collect { content ->
                // 检查是否有数据
                if (content.list.isEmpty() && page == 1) {
                    adapter.clearItems()
                    binding.tvNoData.isVisible = true
                } else {
                    binding.tvNoData.isVisible = false
                    adapter.updateItems(content.list, page)
                }
            }
        }
    }

    private fun getContent(isRefresh: Boolean = true) {
        lifecycleScope.launch {
            try {
                // 调用 ViewModel 获取内容
                viewModel.getContent(lexile, typeId, page, size)
                // 根据 isRefresh 来判断是刷新还是加载更多，更新 UI
                if (isRefresh) {
                    binding.refreshLayout.finishRefresh() // 刷新完成
                } else {
                    binding.refreshLayout.finishLoadMore() // 加载更多完成
                }
            } catch (e: Exception) {
                // 如果发生异常，停止刷新/加载更多动画
                if (isRefresh) {
                    binding.refreshLayout.finishRefresh(false) // 刷新失败
                } else {
                    binding.refreshLayout.finishLoadMore(false) // 加载更多失败
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}