package com.example.hongca

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.io.PrintStream
import java.util.*
import kotlin.collections.ArrayList

class ChoiceFragAdapter (fragmentActivity: FragmentActivity, var viewList: ArrayList<ChoiceTestFragment>) : FragmentStateAdapter(fragmentActivity){

    override fun getItemCount(): Int {
        return viewList.size
    }

    override fun createFragment(position: Int): Fragment {
        return viewList[position]
    }
}