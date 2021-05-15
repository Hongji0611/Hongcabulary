package com.example.hongca

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hongca.databinding.ActivityMainBinding
import com.example.hongca.databinding.ActivityVocaBinding
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList

class VocaActivity : AppCompatActivity() {
    lateinit var binding:ActivityVocaBinding

    var data:ArrayList<MyData> = ArrayList()
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
        binding.title.text = title
        init()
        initData()
        initRecyclerView()
        initTTS()
    }

    private fun init() {
        binding.addvoca.setOnClickListener {
            val intent = Intent(this, AddVocActivity::class.java)
            intent.putExtra("txt", title)
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

    override fun onDestroy() {
        super.onDestroy()
        tts?.shutdown()
    }

    fun readFileScan(scan:Scanner){
        while(scan.hasNextLine()){
            val word = scan.nextLine()
            val meaning = scan.nextLine()
            data.add(MyData(title = title,word = word,meaning = meaning))
        }
        scan.close()
    }

    private fun initData() {
        val temp = "$title.txt"
        try {
            val scan2 = Scanner(openFileInput(temp))
            readFileScan(scan2)
        }catch (e:Exception){
        }
        val scan = Scanner(resources.openRawResource(txt))
        readFileScan(scan)
    }

    private fun initRecyclerView() {
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        adapter = VocaAdapter(data)
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
                adapter.changeIsOpen(position)
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
            }

        }
        val itemTouchHelper = ItemTouchHelper(simpleCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

}