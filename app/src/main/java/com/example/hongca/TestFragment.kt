package com.example.hongca

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.example.hongca.databinding.FragmentTestBinding

class TestFragment : Fragment() {
    var binding:FragmentTestBinding?=null
    var title : String = "즐겨찾기"
    var count : Int = 5
    var type : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTestBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding!!.apply {
            spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    title = "즐겨찾기"
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    title = parent?.getItemAtPosition(position).toString()
                }
            }
            spinner2.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    count = 5
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    count = parent?.getItemAtPosition(position).toString().toInt()
                }
            }
            spinner3.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    type = 0
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    when(parent?.getItemAtPosition(position).toString()){
                        "뜻 맞추기" -> type = 0
                        "영어 맞추기" -> type = 1
                        else -> type = 0
                    }
                }
            }


            choiceTest.setOnClickListener {
                val intent = Intent(activity, ChoiceTestActivity::class.java)
                intent.putExtra("title", title)
                intent.putExtra("count", count)
                intent.putExtra("type", type)
                intent.putExtra("testName", "객관식 퀴즈")
                startActivity(intent)
            }
            writeTest.setOnClickListener {
                val intent = Intent(activity, WriteTestActivity::class.java)
                intent.putExtra("title", title)
                intent.putExtra("count", count)
                intent.putExtra("type", type)
                intent.putExtra("testName", "주관식 퀴즈")
                startActivity(intent)
            }
            IncorrectNote.setOnClickListener {

            }
            listening.setOnClickListener {
                val intent = Intent(activity, ChoiceTestActivity::class.java)
                intent.putExtra("title", title)
                intent.putExtra("count", count)
                intent.putExtra("type", type)
                intent.putExtra("testName", "듣기 평가")
                startActivity(intent)
            }
            listenAndWrite.setOnClickListener {
                val intent = Intent(activity, WriteTestActivity::class.java)
                intent.putExtra("title", title)
                intent.putExtra("count", count)
                intent.putExtra("type", type)
                intent.putExtra("testName", "받아 쓰기")
                startActivity(intent)
            }
            newTest.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSc-wX2Jbnc4OeM5q1rtALjhxvTTL9wauWPwBsXHtJamSBFtpw/viewform?usp=sf_link"));
                startActivity(intent);
            }

        }
    }



}