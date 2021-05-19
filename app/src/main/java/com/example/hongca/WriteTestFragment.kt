package com.example.hongca

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.hongca.databinding.FragmentChoiceTestBinding
import com.example.hongca.databinding.FragmentWriteTestBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.PrintStream
import java.util.*

class WriteTestFragment(val type:Int, val data:MyData, val str:List<String>, val testName:String) : Fragment() {
    var binding: FragmentWriteTestBinding? = null
    var correct: Boolean = false
    //type, data[numList[i]], temp
    var tts: TextToSpeech?= null
    var isTtsReady = false

    lateinit var rdb: DatabaseReference //오답노트 단어장

    fun addWord(){
        rdb.child(data.word).setValue(data)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rdb = FirebaseDatabase.getInstance().getReference("MyApp/Note/오답노트")

        binding = FragmentWriteTestBinding.inflate(layoutInflater, container, false)
        binding!!.apply {
            initTTS()

            if(testName == "주관식 퀴즈") {
                if (type == 0) { //뜻 맞추기
                    question.text = data.word //문제
                } else { //영어 맞추기
                    question.text = data.meaning //문제
                }
            }else{
                question.text = "음성 듣기"
                question.setOnClickListener {
                    if (isTtsReady)
                        tts?.speak(data.word, TextToSpeech.QUEUE_ADD, null, null)
                }
            }

            next.setOnClickListener {
                val userResult = answer.text.toString()
                if(type == 0){
                    for (i in 0 until str.size) {
                        if (str[i].trim() == userResult) {
                            correct = true
                            break
                        }
                        correct = false
                    }
                }else{
                    if(data.word == userResult)
                        correct = true
                    else
                        correct = false
                }

                if (correct)
                    Toast.makeText(activity, "정답입니다!", Toast.LENGTH_SHORT).show()
                else {
                    if (type == 0) {
                        Toast.makeText(
                            activity,
                            "틀렸습니다! 정답은 '${data.meaning}' 입니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(activity, "틀렸습니다! 정답은 '${data.word}' 입니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                    addWord()
                }
            }

        }
        return binding!!.root
    }
    override fun onStop() {
        super.onStop()
        tts?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        tts?.shutdown()
    }
    private fun initTTS() {
        tts = TextToSpeech(activity, TextToSpeech.OnInitListener {
            isTtsReady = true
            tts?.language = Locale.US
        })
    }
}