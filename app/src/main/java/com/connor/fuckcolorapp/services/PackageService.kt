package com.connor.fuckcolorapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.connor.core.emitEvent
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.MainActivity
import com.connor.fuckcolorapp.R
import com.connor.fuckcolorapp.consts.Consts
import com.connor.fuckcolorapp.extension.showToast
import com.connor.fuckcolorapp.models.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class PackageService : LifecycleService() {

    @Inject lateinit var repository: Repository
    @Inject lateinit var app: App

    private val autoUninstall by lazy {
        arrayListOf(
            ""
        )
    }
    private val autoDisable by lazy {
        arrayListOf(
            ""
        )
    }

    override fun onCreate() {
        super.onCreate()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            "package_server", "Package Service",
            NotificationManager.IMPORTANCE_LOW
        )
        manager.createNotificationChannel(channel)
        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, "package_server")
            .setContentTitle("Running...")
            .setContentText("please waiting until finish")
            .setSmallIcon(R.drawable.baseline_ac_unit_24)
            .setContentIntent(pi)
            .build()
        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val isSelect = intent?.extras?.getBoolean("is_select")

        lifecycleScope.launch(Dispatchers.Default) {
            isSelect?.let {
                if (it) selectPure()
                else doAsync()
            }
            withContext(Dispatchers.Main) {
                emitEvent("Finish", Consts.PURE_APP)
                stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private suspend fun doAsync() = coroutineScope {
        val uninstallResult = async {
            autoUninstall.forEach { repository.uninstallAppWithCheck(it) }

        }
        val disableResult = async {
            autoDisable.forEach { repository.disableApp(it) }
        }
        withContext(Dispatchers.IO) {
            uninstallResult.await()
            disableResult.await()
        }
    }

    private suspend fun selectPure() = coroutineScope {
        val uninstallList = app.userAppList.filter { it.isCheck }
        val uninstallResult = async {
            uninstallList.forEach {
                repository.uninstallAppWithCheck(it.packageName.toString())
            }
        }
        val disableList = app.systemAppList.filter { it.isCheck }
        val disableResult = async {
            disableList.forEach {
                repository.disableApp(it.packageName.toString())
            }
        }
        withContext(Dispatchers.IO) {
            uninstallResult.await()
            disableResult.await()
        }
    }
}