package com.fri.uniza.sk.michal.sovcik.chefsrecipies

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class App : Application()
{
    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            "stops_chanel",
            "Stopwatch notification channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}