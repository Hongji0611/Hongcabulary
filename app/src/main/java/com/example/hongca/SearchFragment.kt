package com.example.hongca

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.hongca.databinding.FragmentSearchBinding
import java.io.OutputStreamWriter
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {
    var binding: FragmentSearchBinding?=null
    var findWord = ""

    var data1: ArrayList<MyData> = ArrayList()
    var data2: ArrayList<MyData> = ArrayList()
    var data3: ArrayList<MyData> = ArrayList()

    var isfind = false

    fun readFileScan(scan: Scanner, data:ArrayList<MyData>){
        while(scan.hasNextLine()){
            val word = scan.nextLine()
            val meaning = scan.nextLine()
            val star = scan.nextLine()
            data.add(MyData(word = word,meaning = meaning, star = star))
        }
        scan.close()
    }

    private fun initData() {
        val scan1 = Scanner(resources.openRawResource(R.raw.toeic))
        val scan2 = Scanner(resources.openRawResource(R.raw.toefl))
        try{
            val scan3 = Scanner((activity as MainActivity).openFileInput("나만의 단어장.txt"))
            readFileScan(scan3, data3)
        }catch (e:Exception){}

        readFileScan(scan1, data1)
        readFileScan(scan2, data2)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        initData()
        binding!!.apply {
            daum.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://en.dict.naver.com/#/search?query=${editText.text.toString()}&range=all"));
                startActivity(intent);
            }
            search.setOnClickListener {
                findWord = editText.text.toString()
                layout1.isVisible = false
                layout2.isVisible = false
                layout3.isVisible = false
                isfind = false


                for(i in 0 until data1.size){
                    if((data1[i].word == findWord) || (data1[i].meaning == findWord)){
                        layout1.isVisible = true
                        voca1.text = data1[i].word
                        meaning1.text= data1[i].meaning
                        isfind = true
                        break
                    }
                }
                for(i in 0 until data2.size){
                    if((data2[i].word == findWord) || (data2[i].meaning == findWord)){
                        layout2.isVisible = true
                        voca2.text = data2[i].word
                        meaning2.text= data2[i].meaning
                        isfind = true
                        break
                    }
                }
                for(i in 0 until data3.size){
                    if((data3[i].word == findWord) || (data3[i].meaning == findWord)){
                        layout3.isVisible = true
                        voca3.text = data3[i].word
                        meaning3.text= data3[i].meaning
                        isfind = true
                        break
                    }
                }

                if(!isfind)
                    Toast.makeText(activity, "검색 결과가 존재하지 않습니다", Toast.LENGTH_SHORT).show()

            }
        }
        return binding!!.root
    }

}