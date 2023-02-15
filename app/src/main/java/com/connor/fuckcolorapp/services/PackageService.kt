package com.connor.fuckcolorapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.connor.fuckcolorapp.MainActivity
import com.connor.fuckcolorapp.R
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.models.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class PackageService : LifecycleService() {

    @Inject lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            "ktor_server", "Ktor Service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(channel)
        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, "ktor_server")
            .setContentTitle("Running...")
            .setContentText("please waiting until finish")
            .setSmallIcon(R.drawable.baseline_ac_unit_24)
            .setContentIntent(pi)
            .build()
        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        lifecycleScope.launch(Dispatchers.IO) {
            intent?.extras?.getString("uninstall_package")?.let {
                val result = withContext(Dispatchers.IO) { repository.uninstallAppWithCheck(it) }
                result.logCat()
                stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}