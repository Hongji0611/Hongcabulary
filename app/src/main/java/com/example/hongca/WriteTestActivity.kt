package com.example.hongca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hongca.databinding.ActivityChoiceTestBinding
import com.example.hongca.databinding.ActivityWriteTestBinding
import java.util.*
import kotlin.collections.ArrayList

class WriteTestActivity : AppCompatActivity() {
    lateinit var binding: ActivityWriteTestBinding
    var data:ArrayList<MyData> = ArrayList()
    var viewList = ArrayList<WriteTestFragment>()

    var title = ""
    var count = 0
    var type = 0
    var testName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteTestBinding.inflate(layoutInflater)
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
            "나만의 단어장" -> initData("myown", 0)
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
        val numList =ArrayList<Int>(count)
        while(numList.size<count){
            val num = random.nextInt(data.size)
            if(numList.contains(num)) continue
            numList.add(num)
        }

        for(i in 0 until count){
            //뜻을 /를 기준으로 나눔
            val temp = data[numList[i]].meaning.split("/")
            val frag = WriteTestFragment(type, data[numList[i]], temp, testName)
            viewList.add(frag)
        }

        binding.viewPager.adapter = WriteTestAdapter(this, viewList)
    }
}