package com.example.hongca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hongca.databinding.ActivityChoiceTestBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class ChoiceTestActivity : AppCompatActivity() {
    lateinit var binding: ActivityChoiceTestBinding

    lateinit var rdb: DatabaseReference //문제 가져올 단어장
    var wordList :MutableList<MyData> = mutableListOf()

    lateinit var adapter: VocaAdapter
    var viewList = ArrayList<ChoiceTestFragment>()

    var noteTitle = ""
    var count = 0
    var type = 0
    var testName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoiceTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val i = intent
        noteTitle = i.getStringExtra("title").toString()
        count = i.getIntExtra("count",5)
        type = i.getIntExtra("type",0)
        testName = i.getStringExtra("testName").toString()

        init()
    }

    private fun init() {
        binding.title.text = testName

        binding.noteTitle.text = noteTitle
        binding.count.text = "$count 개"
        if(type == 0){
            binding.type.text = "뜻 맞추기"
        }else{
            binding.type.text = "영어 맞추기"
        }

        //데이터베이스 접근
        Log.e("데이터베이스 이름",noteTitle)
        rdb = FirebaseDatabase.getInstance().getReference("MyApp/Note/${noteTitle}")

        //어뎁터에 값 넣기
        rdb.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(snap in snapshot.children){
                        var data = snap.getValue(MyData::class.java)
                        wordList.add(data!!)
                    }
                }

                Log.e("데이터베이스 사이즈", wordList.size.toString())
                if(wordList.size < 4 ){
                    Toast.makeText(this@ChoiceTestActivity, "단어장의 단어 개수가 적습니다. 더 추가해주세요 :)",Toast.LENGTH_LONG).show()
                    binding.viewPager.adapter = ChoiceFragAdapter(this@ChoiceTestActivity, viewList)
                }else{
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
                            val num = random.nextInt(wordList.size)
                            if(numlist.contains(num)) continue
                            numlist.add(num)
                        }
                        val temp = ArrayList<MyData>()
                        for(j in 0 until 4){
                            temp.add(wordList[numlist[j]])
                        }
                        val frag = ChoiceTestFragment(type, temp, okList[i], i, count-1, testName)
                        viewList.add(frag)
                    }
                    binding.viewPager.adapter = ChoiceFragAdapter(this@ChoiceTestActivity, viewList)
                }
            }
        })
    }
}