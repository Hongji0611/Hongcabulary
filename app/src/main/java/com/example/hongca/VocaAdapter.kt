package com.example.hongca

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.hongca.databinding.Row2Binding
import com.example.hongca.databinding.RowBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class VocaAdapter (options : FirebaseRecyclerOptions<MyData>)
    : FirebaseRecyclerAdapter<MyData, VocaAdapter.ViewHolder>(options) {

    interface OnItemClickListener{
        fun OnItemClick(view:View, position:Int)
    }

    var itemClickListener:OnItemClickListener? =null
    var HeartClickListener:OnItemClickListener? =null

    var wordFlag = false
    var meanFlag = false


    fun hideword(){
        if(meanFlag){
            meanFlag = false
            wordFlag = true
        }else{
            wordFlag = !wordFlag
        }
        notifyDataSetChanged()
    }
    fun hideMean(){
        if(wordFlag){
            wordFlag = false
            meanFlag = true
        }else{
            meanFlag = !meanFlag
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: RowBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.apply {
                voca.setOnClickListener {
                    itemClickListener?.OnItemClick(it, adapterPosition)
                }
                star.setOnClickListener {
                    HeartClickListener?.OnItemClick(it, adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: VocaAdapter.ViewHolder, position: Int, model: MyData) {
        holder.binding.apply {
            voca.text = model.word
            meaning.text = model.meaning
            voca.isVisible = !wordFlag
            meaning.isVisible = !meanFlag

            if(model.star == "true")
                star.setImageResource(R.drawable.ic_baseline_favorite_24)
            else
                star.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }
}