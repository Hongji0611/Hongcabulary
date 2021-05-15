package com.example.hongca

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.UserDictionary.Words.addWord
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.hongca.databinding.FragmentChoiceTestBinding
import com.example.hongca.databinding.FragmentTestBinding
import java.io.PrintStream
import java.util.*
import kotlin.collections.ArrayList

class ChoiceTestFragment(val type:Int, val data:ArrayList<MyData>, val ok:Int, val num:Int, val count:Int, val testName:String) : Fragment() {
    var binding: FragmentChoiceTestBinding?=null
    //data[0] 이 문제이자 정답, 나머지 3개가 오답
    var tts: TextToSpeech?= null
    var isTtsReady = false

    fun addWord(){
        val output = PrintStream(context?.openFileOutput("incorrect.txt", Context.MODE_APPEND))
        output.println(data[0].word)
        output.println(data[0].meaning)
        output.close()
    }
    fun correct(){
        when(ok){
            1-> binding?.answer1?.setBackgroundResource(R.drawable.pink_fill)
            2-> binding?.answer2?.setBackgroundResource(R.drawable.pink_fill)
            3-> binding?.answer3?.setBackgroundResource(R.drawable.pink_fill)
            4-> binding?.answer4?.setBackgroundResource(R.drawable.pink_fill)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChoiceTestBinding.inflate(layoutInflater, container, false)
        binding!!.apply {
            initTTS()
            if(type==0){ //뜻 맞추기
                if(testName == "객관식 퀴즈")
                    question.text = data[0].word //문제
                else{
                    question.text = "음성 듣기"
                    question.setOnClickListener {
                        if (isTtsReady)
                            tts?.speak(data[0].word, TextToSpeech.QUEUE_ADD, null, null)
                    }
                }
                when(ok){ //정답 넣기
                    1 -> {
                        answer1.text = data[0].meaning //정답

                        answer2.text = data[1].meaning
                        answer3.text = data[2].meaning
                        answer4.text = data[3].meaning
                    }
                    2 -> {
                        answer2.text = data[0].meaning //정답

                        answer1.text = data[1].meaning
                        answer3.text = data[2].meaning
                        answer4.text = data[3].meaning
                    }
                    3 -> {
                        answer3.text = data[0].meaning //정답

                        answer1.text = data[1].meaning
                        answer2.text = data[2].meaning
                        answer4.text = data[3].meaning
                    }
                    4 -> {
                        answer4.text = data[0].meaning //정답

                        answer1.text = data[1].meaning
                        answer2.text = data[2].meaning
                        answer3.text = data[3].meaning
                    }
                }
            }else{ //영어 맞추기
                if(testName == "객관식 퀴즈")
                    question.text = data[0].meaning //문제 //문제
                else{
                    question.text = "음성 듣기"
                    question.setOnClickListener {
                        if (isTtsReady)
                            tts?.speak(data[0].word, TextToSpeech.QUEUE_ADD, null, null)
                    }
                }
                when(ok){ //정답 넣기
                    1 -> {
                        answer1.text = data[0].word //정답

                        answer2.text = data[1].word
                        answer3.text = data[2].word
                        answer4.text = data[3].word
                    }
                    2 -> {
                        answer2.text = data[0].word //정답

                        answer1.text = data[1].word
                        answer3.text = data[2].word
                        answer4.text = data[3].word
                    }
                    3 -> {
                        answer3.text = data[0].word //정답

                        answer1.text = data[1].word
                        answer2.text = data[2].word
                        answer4.text = data[3].word
                    }
                    4 -> {
                        answer4.text = data[0].word //정답

                        answer1.text = data[1].word
                        answer2.text = data[2].word
                        answer3.text = data[3].word
                    }
                }
            }
            answer1.setOnClickListener {
                if(ok == 1){
                    Toast.makeText(activity, "정답입니다!", Toast.LENGTH_SHORT).show()
                }else{
                    addWord()
                    Toast.makeText(activity, "틀렸습니다!", Toast.LENGTH_SHORT).show()
                }
                correct()
            }
            answer2.setOnClickListener {
                if(ok == 2){
                    Toast.makeText(activity, "정답입니다!", Toast.LENGTH_SHORT).show()
                }else{
                    addWord()
                    Toast.makeText(activity, "틀렸습니다!", Toast.LENGTH_SHORT).show()
                }
                correct()
            }
            answer3.setOnClickListener {
                if(ok == 3){
                    Toast.makeText(activity, "정답입니다!", Toast.LENGTH_SHORT).show()
                }else{
                    addWord()
                    Toast.makeText(activity, "틀렸습니다!", Toast.LENGTH_SHORT).show()
                }
                correct()
            }
            answer4.setOnClickListener {
                if(ok == 4){
                    Toast.makeText(activity, "정답입니다!", Toast.LENGTH_SHORT).show()
                }else{
                    addWord()
                    Toast.makeText(activity, "틀렸습니다!", Toast.LENGTH_SHORT).show()
                }
                correct()
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