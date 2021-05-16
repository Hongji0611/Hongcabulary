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
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class VocaActivity : AppCompatActivity() {
    lateinit var binding:ActivityVocaBinding

    var data:ArrayList<MyData> = ArrayList()
    var stardata:ArrayList<MyData> = ArrayList()

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: VocaAdapter

    var tts: TextToSpeech?= null
    var isTtsReady = false
    var txt:Int = 0
    var title:String =""

    val ADD_VOC_REQUEST = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVocaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val i = intent
        txt = i.getIntExtra("txt",-1)
        title = i.getStringExtra("title").toString()

        binding.addvoca.isVisible = !(title == "토익" || title == "토플")

        binding.title.text = title
        init()
        initData()
        initRecyclerView()
        initTTS()
    }

    private fun init() {
        binding.addvoca.setOnClickListener {
            val intent = Intent(this, AddVocActivity::class.java)
            intent.putExtra("title", title)
            startActivityForResult(intent, ADD_VOC_REQUEST)
        }

        binding.hidemean.setOnClickListener {
            adapter.hideMean()
        }
        binding.hidevoca.setOnClickListener {
            adapter.hideword()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            ADD_VOC_REQUEST -> {
                if(resultCode== Activity.RESULT_OK){
                    val str = data?.getSerializableExtra("voc") as MyData
                    adapter.addData(str)
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

    override fun onStop() {
        super.onStop()
        tts?.stop()
    }

    fun saveData(){
        var temp = ""
        if(title == "토익" || title == "토플"){

        }else{
            temp = "$title.txt"
            val output = PrintStream(this?.openFileOutput(temp, Context.MODE_PRIVATE))
            for(i in 0 until data.size){
                output.println(data[i].word)
                output.println(data[i].meaning)
                output.println(data[i].star)
            }
            output.close()
        }


    }

    fun saveStar(){
        val temp = "즐겨찾기.txt"
        val output = PrintStream(this?.openFileOutput(temp, Context.MODE_PRIVATE))
        for(i in 0 until stardata.size){
            output.println(stardata[i].word)
            output.println(stardata[i].meaning)
            output.println("true")
        }
        output.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        tts?.shutdown()
    }

    fun readFileScan(scan:Scanner , data:ArrayList<MyData>){
        while(scan.hasNextLine()){
            val word = scan.nextLine()
            val meaning = scan.nextLine()
            val star = scan.nextLine()
            data.add(MyData(word = word,meaning = meaning, star = star))
        }
        scan.close()
    }

    private fun initData() {
        if(title == "토익" || title == "토플") {
            val scan = Scanner(resources.openRawResource(txt))
            readFileScan(scan,data)
        }else{
            val temp = "$title.txt"
            try {
                val scan2 = Scanner(openFileInput(temp))
                readFileScan(scan2,data)
            }catch (e:Exception){
            }
        }
        try{
            val scan3 = Scanner(openFileInput("즐겨찾기.txt"))
            readFileScan(scan3,stardata)
        }catch (e:Exception){}

    }

    private fun initRecyclerView() {
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        adapter = VocaAdapter(data, stardata)
        adapter.itemClickListener = object :VocaAdapter.OnItemClickListener{
            override fun OnItemClick(
                    holder: VocaAdapter.ViewHolder,
                    view: View,
                    data: MyData,
                    position: Int
            ) {
                if (isTtsReady)
                    tts?.speak(data.word, TextToSpeech.QUEUE_ADD, null, null)
            }
        }
        adapter.HeartClickListener = object :VocaAdapter.OnItemClickListener{
            override fun OnItemClick(
                    holder: VocaAdapter.ViewHolder,
                    view: View,
                    data: MyData,
                    position: Int
            ) {
                var flag = false
                if(title == "토익" || title == "토플") {
                    flag = true
                }
                adapter.changeIsOpen(position, flag)
                saveStar()
                saveData()
            }
        }

        recyclerView.adapter = adapter
        val simpleCallBack = object:
                ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN or ItemTouchHelper.UP, ItemTouchHelper.RIGHT){
            override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeItem(viewHolder.adapterPosition)
                saveData()
            }

        }
        val itemTouchHelper = ItemTouchHelper(simpleCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

}