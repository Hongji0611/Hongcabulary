package com.example.hongca

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList
import androidx.fragment.app.FragmentManager


class WordFragment : Fragment() {
    private var columnCount = 2
    var data = ArrayList<TitleData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments!=null){
            data = requireArguments().getSerializable("data") as ArrayList<TitleData>
            Log.d("getData",data[0].title)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initData()

        val view = inflater.inflate(R.layout.fragment_word_list, container, false)
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = WordAdapter(data)
                (adapter as WordAdapter).itemClickListener = object :WordAdapter.OnItemClickListener{
                    override fun OnItemClick(
                        holder: WordAdapter.ViewHolder,
                        view: View,
                        data: TitleData,
                        position: Int
                    ) {
                        val intent = Intent(activity, VocaActivity::class.java)
                        intent.putExtra("txt", data.txt)
                        intent.putExtra("title", data.title)
                        startActivity(intent)

                    }

                }

            }
        }
        return view
    }

    private fun initData() {
        data.add(TitleData("즐겨찾기",0))
        data.add(TitleData("토익",R.raw.toeic))
        data.add(TitleData("토플",R.raw.toefl))
        data.add(TitleData("나만의 단어장",0))
        data.add(TitleData("오답노트",0))
    }
    
}
