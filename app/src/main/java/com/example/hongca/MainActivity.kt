package com.example.hongca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hongca.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val textarr = arrayListOf<String>("단어장","퀴즈", "검색하기", "알림설정")
    val iconarr = arrayListOf<Int>(R.drawable.ic_baseline_menu_book_24, R.drawable.ic_baseline_done_outline_24, R.drawable.ic_baseline_search_24, R.drawable.ic_baseline_alarm_24)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.viewPager.adapter = MyFragStateAdapter(this)
        //Tab, view 연결
        TabLayoutMediator(binding.tablayout, binding.viewPager){
                tab, position ->
            tab.text = textarr[position]
            tab.setIcon(iconarr[position])
        }.attach()
    }
}