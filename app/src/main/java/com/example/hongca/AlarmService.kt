package com.example.hongca

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat

class AlarmService : Service(){
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
        val builder = this?.let {
            NotificationCompat.Builder(it, id)
                    .setSmallIcon(R.drawable.logo2)
                    .setContentTitle("일정 알람")
                    .setContentText("asdfasdfasdf")
                    .setAutoCancel(true)
        }

        //알림 메세지 확인후 앱으로 이동하는 인텐트 생성
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        //빌더 (알림창)에 팬딩 인텐트(인텐트를 전달해주는)를 달기
        val pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder?.setContentIntent(pendingIntent)

        //알림 메세지 전송
        val manager = (this as MainActivity).getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)
        val notification = builder?.build()
        manager.notify(10, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "알람이 울립니다.", Toast.LENGTH_SHORT).show()
        return Service.START_STICKY
    }
    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet")
    }
}