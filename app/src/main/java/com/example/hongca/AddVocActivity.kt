package com.example.hongca

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hongca.databinding.ActivityAddVocBinding
import com.example.hongca.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.PrintStream

class AddVocActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddVocBinding
    lateinit var rdb: DatabaseReference
    var noteTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_voc)
        binding = ActivityAddVocBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val i = intent
        noteTitle = i.getStringExtra("noteTitle").toString()
        init()
    }

    private fun init() {
        binding.addbtn.setOnClickListener {
            val word = binding.word.text.toString()
            val meaning = binding.mean.text.toString()

            rdb = FirebaseDatabase.getInstance().getReference("MyApp/Note/${noteTitle}")
            val item = MyData(word,meaning,"false", noteTitle)
            rdb.child(item.word).setValue(item)

            val intent = Intent()
            intent.putExtra("voc",item)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}