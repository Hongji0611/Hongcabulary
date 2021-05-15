package com.example.hongca

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class VocaAdapter (val items:ArrayList<MyData>, val staritems:ArrayList<MyData>) : RecyclerView.Adapter<VocaAdapter.ViewHolder>(){
    interface OnItemClickListener{
        fun OnItemClick(holder:ViewHolder, view: View, data:MyData, position:Int)
    }

    var itemClickListener:OnItemClickListener? =null
    var HeartClickListener:OnItemClickListener? =null
    var wordFlag = false
    var meanFlag = false

    fun changeIsOpen(pos:Int){
        if(items[pos].star == "false"){
            items[pos].star = "true"
            staritems.add(items[pos])
        }else{
            items[pos].star = "false"
            staritems.remove(items[pos])
        }
        notifyDataSetChanged()
    }

    fun moveItem(oldPos:Int, newPos:Int){
        val item = items[oldPos]
        items.removeAt(oldPos)
        items.add(newPos, item)
        notifyItemMoved(oldPos,newPos)
    }

    fun removeItem(pos:Int){
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }
    fun addData(data:MyData){
        items.add(data)
        notifyDataSetChanged()
    }

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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.findViewById(R.id.voca)
        val meaningView: TextView = itemView.findViewById(R.id.meaning)
        val imageView:ImageView = itemView.findViewById(R.id.star)
        init{
            textView.setOnClickListener{
                itemClickListener?.OnItemClick(this,it,items[adapterPosition],adapterPosition)
            }
            imageView.setOnClickListener {
                HeartClickListener?.OnItemClick(this,it,items[adapterPosition],adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position].word
        holder.meaningView.text = items[position].meaning
        holder.textView.isVisible = !wordFlag
        holder.meaningView.isVisible = !meanFlag
        if(items[position].star == "true")
            holder.imageView.setImageResource(R.drawable.ic_baseline_favorite_24)
        else
            holder.imageView.setImageResource(R.drawable.ic_baseline_favorite_border_24)
    }
}