package com.example.mvvm_templete

import android.Manifest
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.mvvm_templete.common.Utils
import com.example.mvvm_templete.service.ForegroundService
import dagger.hilt.android.HiltAndroidApp
import java.util.Timer
import kotlin.concurrent.schedule

@HiltAndroidApp
class AppApplication : Application() {
    companion object {
        val TAG: String = javaClass.name
        val TIME_BINE: Long = 50L
        lateinit var context: Context
        var mService: ForegroundService? = null

        val mServiceConnection: ServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                try {
                    val binder: ForegroundService.LocalBinder =
                        service as ForegroundService.LocalBinder
                    mService = binder.service
                    if (!Utils.serviceIsRunningInForeground(context) && Utils.hasPermissions(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    ) {
                        mService?.requestStartService()
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "onServiceConnected fail : ${e.message}")
                }
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                mService = null
            }

        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        Timer().schedule(TIME_BINE) {
            bind()
        }
    }

    private fun bind() {
        val con = ComponentName(packageName, "$packageName.service.ForegroundService")

        val serviceIntent = Intent(
            applicationContext,
            ForegroundService::class.java
        ).apply { component = con }

        bindService(
            serviceIntent,
            mServiceConnection,
            BIND_AUTO_CREATE
        )
    }
}