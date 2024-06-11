package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class StopwatchService : Service() {

    private val binder = LocalBinder()
    private var job: Job? = null
    private var _seconds:MutableStateFlow<Int> = MutableStateFlow<Int>(0)
    val seconds = _seconds.asStateFlow()
    companion object{
        val TIMEKEY = "time"
    }
    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
    inner class LocalBinder : Binder()
    {
        fun getService():StopwatchService = this@StopwatchService;
    }

    enum class Actions{
        START,
        STOP
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    private suspend fun startTimer() {

    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val time = intent?.extras?.getInt(TIMEKEY)
        when(intent?.action) {
            Actions.START.toString() -> {

                time?.let {
                    _seconds.value = it
                    start(getTime(it),false)
                    job = CoroutineScope(Dispatchers.Main).launch{
                        while (isActive && _seconds.value != 0) {
                            delay(1000)
                            _seconds.value--
                            start(getTime( _seconds.value),false)
                        }
                        start(getTime(_seconds.value), true)
                    }
                }
            }
            Actions.STOP.toString() ->{
                job?.cancel()
                stopSelf()
            }
        }


        return super.onStartCommand(intent, flags, startId)
    }
    private fun getTime(seconds:Int) : String {
        return ((seconds / (60)) % 24).toString().padStart(2,'0') + ":" +
                ((seconds) % 60).toString().padStart(2,'0')
    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
        job?.cancel()
        stopSelf()
    }
    private fun start(text: String, last:Boolean) {

        var notification = NotificationCompat.Builder(this, "stops_chanel").
        setContentTitle("Timer")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText(text)
            .setSilent(!last)

        startForeground(1 ,notification.build())

    }
}