package com.example.yyscdemo.demo2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.yyscdemo.R
import com.example.yyscdemo.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private val titles by lazy {
        listOf("首页", "听力", "文库", "我的")
    }
    private val fragments by lazy {
        listOf(
            BlankFragment(), BlankFragment(), SubjectFragment(), BlankFragment()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
    }

    private fun initUI() {
        binding.vpHome.adapter = MyPagerAdapter(this)
        binding.tlHome.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let {
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        TabLayoutMediator(binding.tlHome, binding.vpHome) { tab, position ->
            // 应该是要设置customView 的,这里简单一点就好(主要是懒的弄图片了)
            tab.text = titles[position]
        }.attach()
    }

    inner class MyPagerAdapter(fActivity: FragmentActivity) : FragmentStateAdapter(fActivity) {
        override fun getItemCount() = titles.size
        override fun createFragment(position: Int) = fragments[position]
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            return intent
        }
    }
}