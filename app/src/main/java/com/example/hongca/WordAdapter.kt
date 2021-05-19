package com.example.hongca

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hongca.databinding.Row2Binding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class WordAdapter (options : FirebaseRecyclerOptions<String>)
    : FirebaseRecyclerAdapter<String, WordAdapter.ViewHolder>(options) {

    interface OnItemClickListener{
        fun OnItemClick(view:View, position:Int)
    }

    var itemClickListener:OnItemClickListener? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = Row2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }


    inner class ViewHolder(val binding: Row2Binding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClickListener?.OnItemClick(it, adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: String) {
        holder.binding.apply{
            noteTitle.text = model
        }
    }
}
