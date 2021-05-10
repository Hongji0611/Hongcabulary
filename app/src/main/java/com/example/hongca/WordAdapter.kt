package com.example.hongca

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WordAdapter (
    val values: ArrayList<TitleData>
) : RecyclerView.Adapter<WordAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(holder:ViewHolder, view:View, data:TitleData, position:Int)
    }

    var itemClickListener:OnItemClickListener? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_word, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.contentView.text = item.title
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contentView: TextView = view.findViewById(R.id.content)

        init{
            itemView.setOnClickListener{
                itemClickListener?.OnItemClick(this,it,values[adapterPosition],adapterPosition)
            }
        }
    }
}
