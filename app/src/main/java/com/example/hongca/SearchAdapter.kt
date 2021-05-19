package com.example.hongca

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.hongca.databinding.Row2Binding
import com.example.hongca.databinding.Row3Binding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class SearchAdapter (options : FirebaseRecyclerOptions<MyData>)
    : FirebaseRecyclerAdapter<MyData, SearchAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = Row3Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }


    inner class ViewHolder(val binding: Row3Binding): RecyclerView.ViewHolder(binding.root) {
        init {

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: MyData) {
        holder.binding.apply {
            voca.text = model.word
            meaning.text = model.meaning
            noteTitle.text = model.noteTitle
        }
    }
}
