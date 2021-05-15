package com.example.hongca

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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
                        val timerTask = object  : TimerTask(){
                            override fun run() {
                                makeNotification() //알림창 수행

                            }
                        }
                        val timer = Timer()
                        timer.schedule(timerTask, 2000)
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
    fun makeNotification(){
        //알림창 기본 정보 지정
        val id = "myChannel"
        val name = "TimeCheckChannel"
        val notificationChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.enableVibration(true)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE


        //알림창 빌더도 세부 내용 지정
        val builder = activity?.let {
            NotificationCompat.Builder(it, id)
                .setSmallIcon(R.drawable.logo2)
                .setContentTitle("일정 알람")
                .setContentText(message)
                .setAutoCancel(true)
        }

    //알림 메세지 확인후 앱으로 이동하는 인텐트 생성
        val intent = Intent(activity, MainActivity::class.java)
        intent.putExtra("time",message)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        //빌더 (알림창)에 팬딩 인텐트(인텐트를 전달해주는)를 달기
        val pendingIntent = PendingIntent.getActivity(activity, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder?.setContentIntent(pendingIntent)

    //알림 메세지 전송
        val manager = (activity as MainActivity).getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)
        val notification = builder?.build()
        manager.notify(10, notification)
    }

}