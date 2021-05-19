package com.example.hongca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hongca.databinding.ActivityWriteTestBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class WriteTestActivity : AppCompatActivity() {
    lateinit var binding: ActivityWriteTestBinding
    lateinit var rdb: DatabaseReference //문제 가져올 단어장

    var wordList = ArrayList<MyData>()
    var viewList = ArrayList<WriteTestFragment>()

    var noteTitle = ""
    var count = 0
    var type = 0
    var testName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        val i = intent
        noteTitle = i.getStringExtra("title").toString()
        count = i.getIntExtra("count",5)
        type = i.getIntExtra("type",0)
        testName = i.getStringExtra("testName").toString()

        binding.title.text = testName

        binding.noteTitle.text = noteTitle
        binding.count.text = "$count 개"
        if(type == 0){
            binding.type.text = "뜻 맞추기"
        }else{
            binding.type.text = "영어 맞추기"
        }

        //데이터베이스 접근
        rdb = FirebaseDatabase.getInstance().getReference("MyApp/Note/${noteTitle}")
        val query = rdb.limitToLast(50)
        val option = FirebaseRecyclerOptions.Builder<MyData>()
                .setQuery(query, MyData::class.java)
                .build()

        //어뎁터에 값 넣기
        rdb.addListenerForSingleValueEvent(object:ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        var data = snap.getValue(MyData::class.java)
                        wordList.add(data!!)
                    }
                    if(wordList.size < count){
                        count = wordList.size
                    }

                    //중복되지 않게 단어를 뽑음
                    val random = Random()
                    val numList =ArrayList<Int>(count)
                    while(numList.size<count){
                        val num = random.nextInt(wordList.size)
                        if(numList.contains(num)) continue
                        numList.add(num)
                    }

                    for(i in 0 until count){
                        //뜻을 /를 기준으로 나눔
                        val temp = wordList[numList[i]].meaning.split("/")
                        val frag = WriteTestFragment(type, wordList[numList[i]], temp, testName)
                        viewList.add(frag)
                    }

                    binding.viewPager.adapter = WriteTestAdapter(this@WriteTestActivity, viewList)
                }
            }
        })

    }
}