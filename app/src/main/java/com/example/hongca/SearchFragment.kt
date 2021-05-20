package com.example.hongca

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hongca.databinding.FragmentSearchBinding
import com.example.hongca.databinding.FragmentWordBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {
    var binding: FragmentSearchBinding?=null

    var isfind = false

    lateinit var rdb: DatabaseReference
    lateinit var rdb2: DatabaseReference
    lateinit var rdb3: DatabaseReference

    var adapter: SearchAdapter? = null
    var adapter2: SearchAdapter? = null
    var adapter3: SearchAdapter? = null

    override fun onDestroy() {
        super.onDestroy()
        adapter?.stopListening()
        adapter2?.stopListening()
        adapter3?.stopListening()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        //데이터베이스 접근
        rdb = FirebaseDatabase.getInstance().getReference("MyApp/Note/토익")
        rdb2 = FirebaseDatabase.getInstance().getReference("MyApp/Note/토플")
        rdb3 = FirebaseDatabase.getInstance().getReference("MyApp/Note/나만의 단어장")

        //리사이클러뷰 설정
        binding?.apply {
            recyclerView1.layoutManager = LinearLayoutManager(activity)
            recyclerView2.layoutManager = LinearLayoutManager(activity)
            recyclerView3.layoutManager = LinearLayoutManager(activity)

            daum.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://en.dict.naver.com/#/search?query=${editText.text.toString()}&range=all"));
                startActivity(intent);
            }

            search.setOnClickListener {
                if(adapter!= null){
                    adapter!!.stopListening()
                }
                if(adapter2!= null){
                    adapter2!!.stopListening()
                }
                if(adapter3!= null){
                    adapter3!!.stopListening()
                }

                val query = rdb.orderByChild("word").equalTo(editText.text.toString())
                val option = FirebaseRecyclerOptions.Builder<MyData>()
                        .setQuery(query, MyData::class.java)
                        .build()
                adapter = SearchAdapter(option)
                binding!!.recyclerView1.adapter = adapter
                adapter?.startListening()

                val query2 = rdb2.orderByChild("word").equalTo(editText.text.toString())
                val option2 = FirebaseRecyclerOptions.Builder<MyData>()
                        .setQuery(query2, MyData::class.java)
                        .build()
                adapter2 = SearchAdapter(option2)

                binding!!.recyclerView2.adapter = adapter2
                adapter2?.startListening()

                val query3 = rdb3.orderByChild("word").equalTo(editText.text.toString())
                val option3 = FirebaseRecyclerOptions.Builder<MyData>()
                        .setQuery(query3, MyData::class.java)
                        .build()
                adapter3 = SearchAdapter(option3)

                binding!!.recyclerView3.adapter = adapter3
                adapter3?.startListening()

                editText.text.clear()
            }
        }
        return binding!!.root
    }

}