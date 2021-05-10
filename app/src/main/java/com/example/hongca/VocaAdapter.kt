package com.example.hongca

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class VocaAdapter (val items:ArrayList<MyData>) : RecyclerView.Adapter<VocaAdapter.ViewHolder>(){
    interface OnItemClickListener{
        fun OnItemClick(holder:ViewHolder, view: View, data:MyData, position:Int)
    }

    var itemClickListener:OnItemClickListener? =null

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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.findViewById(R.id.voca)
        val meaningView: TextView = itemView.findViewById(R.id.meaning)
        init{
            itemView.setOnClickListener{
                itemClickListener?.OnItemClick(this,it,items[adapterPosition],adapterPosition)
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
    }
}