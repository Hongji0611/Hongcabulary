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
    var findWord = ""

    var isfind = false
    lateinit var rdb: DatabaseReference
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: WordAdapter

    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        //데이터베이스 접근
        rdb = FirebaseDatabase.getInstance().getReference("MyApp/Note/토익")


        return binding!!.root
    }

}