package com.example.mvvm_templete.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.mvvm_templete.R
import com.example.mvvm_templete.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForegroundService : Service() {
    companion object {
        private val TAG = ForegroundService::class.java.simpleName
        const val NOTIFICATION_ID = 12345678
        const val CHANNEL_ID = "Test"
        const val CHANNEL_NAME = "Test"
    }

    inner class LocalBinder : Binder() {
        val service: ForegroundService
            get() = this@ForegroundService
    }

    private val mBinder: IBinder = LocalBinder()

    private lateinit var mNotificationManager: NotificationManager

    private val notification: Notification
        get() {
            val intent = Intent(this, MainActivity::class.java)
            intent.action = Intent.ACTION_MAIN
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            val activityPendingIntent = PendingIntent.getActivity(
                this, 0,
                intent, PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.noti))
                .setContentIntent(activityPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSmallIcon(R.mipmap.ic_launcher)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId(CHANNEL_ID)
            }
            return builder.build()
        }

    override fun onBind(p0: Intent?): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_MIN
            )
            mNotificationManager.createNotificationChannel(mChannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, notification)
        return START_STICKY
    }

    fun requestStartService() {
        CoroutineScope(Dispatchers.Default).launch {
            val intent = Intent(applicationContext, ForegroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            startForeground(NOTIFICATION_ID, notification)
        }
    }
}
