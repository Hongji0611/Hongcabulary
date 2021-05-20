package com.example.hongca

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.hongca.databinding.FragmentAlarmBinding
import com.example.hongca.databinding.MypickerdlgBinding
import java.util.*


class AlarmFragment() : Fragment() {
    var binding: FragmentAlarmBinding?=null
    var mymemo = ""
    var myhour = 0
    var mymin = 0
    var message = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmBinding.inflate(layoutInflater, container, false)

        binding!!.apply {
            calendarView.setOnDateChangeListener { view, year, month, dayOfMonth -> // 시간 설정 Alert창 만들기
                val dlgBinding = MypickerdlgBinding.inflate(layoutInflater)
                val dlgBuilder = activity?.let { AlertDialog.Builder(it) }
                dlgBuilder?.setView(dlgBinding.root)
                    ?.setPositiveButton("추가"){
                            _, _ ->
                        mymemo = dlgBinding.editText.text.toString()
                        myhour = dlgBinding.timePicker.hour
                        mymin = dlgBinding.timePicker.minute
                        message = myhour.toString() + "시 "+mymin.toString()+"분: "+mymemo

                        //알람 시간 설정
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = System.currentTimeMillis()
                        calendar.set(year,month,dayOfMonth,myhour,mymin,0)

                        //알람 Receiver 설정
                        val intent = Intent(activity, AlarmReceiver::class.java)
                        intent.putExtra("mymemo", mymemo)
                        var pIntent = PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

                        //알람 메니저 설정
                        var alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pIntent)

                        if(calendar.before(Calendar.getInstance())){
                            Toast.makeText(activity, "알림을 설정할 수 없습니다.",Toast.LENGTH_SHORT).show()
                        }

                        Toast.makeText(activity, "알림이 추가되었습니다",Toast.LENGTH_SHORT).show()
                    }
                    ?.setNegativeButton("취소"){
                            _, _ ->
                    }
                    ?.show()
            }

        }
        return binding!!.root
    }
}