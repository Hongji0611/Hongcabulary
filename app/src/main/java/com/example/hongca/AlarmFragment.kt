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

//                        val timerTask = object  : TimerTask(){
//                            override fun run() {
//                                makeNotification() //알림창 수행
//
//                            }
//                        }

                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = System.currentTimeMillis()
                        calendar.set(year,month,dayOfMonth,myhour,mymin)


                        val intent = Intent(activity, AlarmReceiver::class.java).apply {
                            action = "com.check.up.setAlarm"
                        }

                        val alarmMgr:AlarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        val pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, 0)
                        alarmMgr!![AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()] = pendingIntent

                        if(calendar.before(Calendar.getInstance())){
                            Toast.makeText(activity, "알림을 설정할 수 없습니다.",Toast.LENGTH_SHORT).show()
                        }

//                        val timer = Timer()
//                        timer.schedule(timerTask, 2000)
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