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
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "알람이 울립니다.", Toast.LENGTH_SHORT).show()
        return Service.START_STICKY
    }
    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet")
    }
}