package com.example.hongca

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hongca.databinding.FragmentWordBinding
import com.example.hongca.databinding.FragmentWriteTestBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.collections.ArrayList


class WordFragment : Fragment() {
    var data = ArrayList<String>()

    var binding: FragmentWordBinding? = null
    lateinit var rdb: DatabaseReference
    lateinit var layoutManager: GridLayoutManager
    lateinit var adapter: WordAdapter

    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWordBinding.inflate(layoutInflater, container, false)
        layoutManager = GridLayoutManager(activity, 2)

        //데이터베이스 접근
        rdb = FirebaseDatabase.getInstance().getReference("MyApp/NoteName")
        val query = rdb.limitToLast(50)
        val option = FirebaseRecyclerOptions.Builder<String>()
            .setQuery(query, String::class.java)
            .build()

        //어뎁터 설정
        adapter = WordAdapter(option)
        adapter.startListening()
        adapter.notifyDataSetChanged()
        adapter.itemClickListener = object :WordAdapter.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int) {
                val intent = Intent(activity, VocaActivity::class.java)
                intent.putExtra("noteTitle", adapter.getItem(position))
                startActivity(intent)
            }

        }

        //리사이클러뷰 설정
        binding?.apply {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            addNote.setOnClickListener {
//                val intent = Intent(activity, VocaActivity::class.java)
//                intent.putExtra("noteTitle", "즐겨찾기")
//                startActivity(intent)
            }

        }

        return binding!!.root
    }

}
