package com.example.yyscdemo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.yyscdemo.databinding.ActivityMainBinding
import com.example.yyscdemo.demo1.EditInfoActivity
import com.example.yyscdemo.demo2.HomeActivity

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
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

        binding.apply {
            button1.setOnClickListener {
                startActivity(EditInfoActivity.createIntent(this@MainActivity))
            }
            button2.setOnClickListener {
                startActivity(HomeActivity.createIntent(this@MainActivity))
            }
        }
    }
}