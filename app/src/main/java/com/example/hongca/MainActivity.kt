package com.example.hongca

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hongca.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val textarr = arrayListOf<String>("단어장","퀴즈", "검색하기", "알림설정")
    val iconarr = arrayListOf<Int>(R.drawable.ic_baseline_menu_book_24, R.drawable.ic_baseline_done_outline_24, R.drawable.ic_baseline_search_24, R.drawable.ic_baseline_alarm_24)
    private var data = ArrayList<TitleData>()

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
        val selected = ContextCompat.getColor(this, R.color.pink)
        val unselected = ContextCompat.getColor(this, R.color.black)

        binding.tablayout.getTabAt(0)?.getIcon()?.setColorFilter(selected, PorterDuff.Mode.SRC_IN)
        binding.tablayout.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val position = tab?.position
                if (position != null) {
                    binding.tablayout.getTabAt(position)?.getIcon()?.setColorFilter(unselected, PorterDuff.Mode.SRC_IN)
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                binding.tablayout.getTabAt(position)?.getIcon()?.setColorFilter(selected, PorterDuff.Mode.SRC_IN)
            }
        })

        val wordFragment = WordFragment()
        var args = Bundle()
        args.putSerializable("data",data)
        wordFragment.arguments = args
    }
}