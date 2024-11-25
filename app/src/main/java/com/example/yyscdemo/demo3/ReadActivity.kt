package com.example.yyscdemo.demo3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.yyscdemo.R
import com.example.yyscdemo.click
import com.example.yyscdemo.databinding.ActivityReadBinding
import com.example.yyscdemo.model.ContentList
import kotlinx.coroutines.launch

/**
 * @author : yysc
 * @description : demo3 的 activity
 * 有个问题就是 ,标题的audioUrl 没有给我,我猜应该是自己写tts,
 * 这里还没弄,弄个也挺方便的,逻辑就是标题的 tts 播放之后内容就行
 */
class ReadActivity : AppCompatActivity() {
    private val binding: ActivityReadBinding by lazy {
        ActivityReadBinding.inflate(layoutInflater)
    }
    private var lessonListSize = 0
    private val viewModel by viewModels<ReadActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initData()
        initUI()
    }

    private fun initData() {
        fun setViewVisible(visible: Boolean) {
            binding.titleView.isVisible = visible
            binding.vpRead.isVisible = visible
            binding.tvProgress.isVisible = visible
            binding.pbRead.isVisible = visible
            binding.pbLoading.isVisible = !visible
        }
        lifecycleScope.launch {
            setViewVisible(false)
            viewModel.lessonList.collect { lessonList ->
                lessonList ?: return@collect
                setViewVisible(true)
                binding.vpRead.adapter = MyPagerAdapter(this@ReadActivity, lessonList.contentList)
                binding.tvProgress.text = String.format(
                    getString(R.string.string_lesson_index), 1, lessonList.contentList.size
                )
                binding.titleView.getTitle().text = lessonList.title
                lessonListSize = lessonList.contentList.size
            }
        }
        viewModel.getReadList(6)
    }

    private fun initUI() {
        binding.apply {
            vpRead.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (lessonListSize > 0) {
                        val progress = ((position + 1).toFloat() / lessonListSize) * 100
                        binding.apply {
                            pbRead.setProgress(progress)
                            tvProgress.text = String.format(
                                getString(R.string.string_lesson_index),
                                position + 1,
                                lessonListSize
                            )
                        }
                    }
                }
            })
            titleView.getLeftArrow().click {
                finishSafely()
            }
        }
    }

    inner class MyPagerAdapter(
        fragmentActivity: FragmentActivity, private val contentList: ArrayList<ContentList>
    ) : FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int {
            return contentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return ReadFragment.newInstance(position, contentList)
        }
    }

    private fun finishSafely() {
        if (!isFinishing) {
            finish()
        }
        overridePendingTransition(0, 0)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, ReadActivity::class.java)
            return intent
        }
    }

}