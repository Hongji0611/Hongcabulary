package com.example.hongca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.hongca.databinding.ActivityChoiceTestBinding
import com.example.hongca.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*
import kotlin.collections.ArrayList

class ChoiceTestActivity : AppCompatActivity() {
    lateinit var binding: ActivityChoiceTestBinding
    var data:ArrayList<MyData> = ArrayList()
    var viewList = ArrayList<ChoiceTestFragment>()

    var title = ""
    var count = 0
    var type = 0
    var testName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoiceTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun readFileScan(scan: Scanner){
        while(scan.hasNextLine()){
            val word = scan.nextLine()
            val meaning = scan.nextLine()
            val star = scan.nextLine()
            data.add(MyData(word = word,meaning = meaning, star = star))
        }
        scan.close()
    }

    private fun initData(title : String, rawsourse:Int) {
        val temp = "$title.txt"
        try {
            val scan2 = Scanner(openFileInput(temp))
            readFileScan(scan2)
        }catch (e:Exception){
        }
        val scan = Scanner(resources.openRawResource(rawsourse))
        readFileScan(scan)
    }

    private fun init() {
        val i = intent
        title = i.getStringExtra("title").toString()
        count = i.getIntExtra("count",5)
        type = i.getIntExtra("type",0)
        testName = i.getStringExtra("testName").toString()

        binding.title.text = testName


        when(title){
            "즐겨찾기" -> initData("star", 0)
            "토익" -> initData("toeic",R.raw.toeic)
            "토플" -> initData("toefl",R.raw.toefl)
            "나만의 단어장" -> initData("myown",0)
        }

        binding.noteTitle.text = title
        binding.count.text = "$count 개"
        if(type == 0){
            binding.type.text = "뜻 맞추기"
        }else{
            binding.type.text = "영어 맞추기"
        }
        //정답 자리 랜덤으로 선택
        val random = Random()
        val okList =ArrayList<Int>(count)
        for (i in 0 until count){
            okList.add(random.nextInt(4)+1)
        }

        for(i in 0 until count){
            //문제 하나 당, 중복 없이 랜덤 숫자 생성
            val numlist = ArrayList<Int>()
            while(numlist.size<4){
                val num = random.nextInt(data.size)
                if(numlist.contains(num)) continue
                numlist.add(num)
            }
            val temp = ArrayList<MyData>()
            for(j in 0 until 4){
                temp.add(data[numlist[j]])
            }
            val frag = ChoiceTestFragment(type, temp, okList[i], i, count-1, testName)
            viewList.add(frag)
        }

        binding.viewPager.adapter = ChoiceFragAdapter(this, viewList)
    }
}