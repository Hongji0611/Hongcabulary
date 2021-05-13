package com.example.hongca

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hongca.databinding.FragmentTestBinding

class TestFragment : Fragment() {
    var binding:FragmentTestBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTestBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding!!.apply {
            choiceTest.setOnClickListener {

            }
            writeTest.setOnClickListener {

            }
            IncorrectNote.setOnClickListener {

            }
            listening.setOnClickListener {

            }
            listenAndWrite.setOnClickListener {

            }
            newTest.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSc-wX2Jbnc4OeM5q1rtALjhxvTTL9wauWPwBsXHtJamSBFtpw/viewform?usp=sf_link"));
                startActivity(intent);
            }

        }
    }

}