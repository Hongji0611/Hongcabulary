package com.example.hongca

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class WriteTestAdapter (fragmentActivity: FragmentActivity, var viewList: ArrayList<WriteTestFragment>) : FragmentStateAdapter(fragmentActivity){

    override fun getItemCount(): Int {
        return viewList.size
    }

    override fun createFragment(position: Int): Fragment {
        return viewList[position]
    }
}