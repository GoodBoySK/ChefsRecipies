package com.fri.uniza.sk.michal.sovcik.chefsrecipies

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


class App : Application()
{
    val dataStorePreferences: DataStore<Preferences> by preferencesDataStore(
        name = "userPreferenes"
    )
    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            "stops_chanel",
            "Stopwatch notification channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        dataStorePreferences.data
    }
}