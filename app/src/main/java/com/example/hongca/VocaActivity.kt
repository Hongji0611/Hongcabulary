package com.example.hongca

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hongca.databinding.ActivityVocaBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class VocaActivity : AppCompatActivity() {
    lateinit var binding:ActivityVocaBinding

    lateinit var adapter: VocaAdapter
    lateinit var layoutManager: LinearLayoutManager

    var tts: TextToSpeech?= null
    var isTtsReady = false

    var noteTitle:String =""
    val ADD_VOC_REQUEST = 100

    lateinit var rdb: DatabaseReference
    lateinit var rdb2: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVocaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val i = intent
        noteTitle = i.getStringExtra("noteTitle").toString()

        binding.title.text = noteTitle
        init()
        initTTS()
    }

    private fun init() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //데이터베이스 접근
        rdb = FirebaseDatabase.getInstance().getReference("MyApp/Note/${noteTitle}")
        val query = rdb.limitToLast(50)
        val option = FirebaseRecyclerOptions.Builder<MyData>()
            .setQuery(query, MyData::class.java)
            .build()

        rdb2 = FirebaseDatabase.getInstance().getReference("MyApp/Note/즐겨찾기")

        //어뎁터 설정
        adapter = VocaAdapter(option)
        adapter.itemClickListener = object :VocaAdapter.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int) {
                if (isTtsReady)
                    tts?.speak(adapter.getItem(position).word, TextToSpeech.QUEUE_ADD, null, null)
            }
        }

        adapter.HeartClickListener = object :VocaAdapter.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int) {
                var temp = adapter.getItem(position)
                if(temp.star == "true"){ //즐겨찾기 해제
                    if(noteTitle == "즐겨찾기" || noteTitle == "오답노트"){
                        val rdb3 = FirebaseDatabase.getInstance().getReference("MyApp/Note/${temp.noteTitle}")
                        rdb3.child(temp.word)
                                .child("star") //일부 속성 바꾸기
                                .setValue("false")
                    }else{
                        rdb.child(temp.word)
                                .child("star") //일부 속성 바꾸기
                                .setValue("false")
                    }
                    temp.star = "false"
                    rdb2.child(temp.word).removeValue()
                }else{ //즐겨찾기 추가
                    rdb.child(temp.word)
                            .child("star") //일부 속성 바꾸기
                            .setValue("true")
                    temp.star = "true"
                    rdb2.child(temp.word).setValue(temp)
                }
            }
        }

        //리사이클러뷰와 각 버튼 설정
        binding.apply {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            hidemean.setOnClickListener {
                adapter.hideMean()
            }
            hidevoca.setOnClickListener {
                adapter.hideword()
            }
        }

        binding.addvoca.setOnClickListener {
            val intent = Intent(this, AddVocActivity::class.java)
            intent.putExtra("noteTitle", noteTitle)
            startActivityForResult(intent, ADD_VOC_REQUEST)
        }

        if(noteTitle != "즐겨찾기"){
            val simpleCallBack = object:
                    ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN or ItemTouchHelper.UP, ItemTouchHelper.RIGHT){
                override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { //데이터 삭제
                    rdb.child(adapter.getItem(viewHolder.adapterPosition).word).removeValue()
                }
            }
            val itemTouchHelper = ItemTouchHelper(simpleCallBack)
            itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            ADD_VOC_REQUEST -> {
                if(resultCode== Activity.RESULT_OK){
                    val str = data?.getSerializableExtra("voc") as MyData
                    Toast.makeText(this, str.word+" 단어 추가완료 :)",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initTTS() {
        tts = TextToSpeech(this, TextToSpeech.OnInitListener {
            isTtsReady = true
            tts?.language = Locale.US
        })
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        tts?.stop()
        adapter.stopListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        tts?.shutdown()
    }

}