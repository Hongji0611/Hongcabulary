package com.example.hongca

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hongca.databinding.ActivityAddVocBinding
import com.example.hongca.databinding.ActivityMainBinding
import java.io.PrintStream

class AddVocActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddVocBinding
    var txt:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_voc)
        binding = ActivityAddVocBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val i = intent
        val title = i.getStringExtra("title").toString()
        txt = "$title.txt"

        init()
    }

    private fun init() {
        binding.addbtn.setOnClickListener {
            val word = binding.word.text.toString()
            val meaning = binding.mean.text.toString()
            writeFile(word, meaning)
        }
    }

    private fun writeFile(word: String, meaning: String) {
        val output = PrintStream(openFileOutput(txt, Context.MODE_APPEND))
        output.println(word)
        output.println(meaning)
        output.close()
        val intent = Intent()
        intent.putExtra("voc",MyData(word = word, meaning = meaning, star = "false"))
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}