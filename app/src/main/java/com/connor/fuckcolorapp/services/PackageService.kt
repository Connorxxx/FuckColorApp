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
import com.connor.fuckcolorapp.extension.logCat
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
            "com.android.email",
            "com.coloros.familyguard",
            "com.heytap.reader",
            "com.oplus.play",
            "com.oppo.store",
            "com.smile.gifmaker",
            "com.autonavi.minimap",
            "com.baidu.searchbox",
            "com.coloros.backuprestore",
            "com.coloros.note",
            "com.coloros.shortcuts",
            "com.dragon.read",
            "com.heytap.smarthome",
           // "com.jingdong.app.mall",
            "com.nearme.gamecenter",
            "com.oneplus.bbs",
           // "com.sina.weibo",
            "com.ss.android.article.news",
          //  "com.ss.android.ugc.aweme",
          //  "com.taobao.taobao",
            "com.tencent.qqlive",
            "com.wuba",
            "com.ximalaya.ting.android",
           // "com.xunmeng.pinduoduo",
            "com.zhihu.android",
            "ctrip.android.view",
          //  "tv.danmaku.bili",
        )
    }
    private val autoDisable by lazy {
        arrayListOf(
            "com.coloros.phonemanager",
            "com.finshell.wallet",
            "com.heytap.music",
            "com.heytap.speechassist",
            "com.heytap.yoli",
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
            repository.checkShizuku {
                isSelect?.let {
                    if (it) selectPure()
                    else doAsync()
                }
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
            autoUninstall.forEach { repository.uninstallApp(it) }

        }
        val disableResult = async {
            autoDisable.forEach { repository.setAppState(it) }
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
                repository.uninstallApp(it.packageName.toString())
            }
        }
        val disableList = app.systemAppList.filter { it.isCheck }
        val disableResult = async {
            disableList.forEach {
                repository.setAppState(it.packageName.toString())
            }
        }
        val disableAllList = app.allAppList.filter { it.isCheck }
        val disableAllResult = async {
            disableAllList.size.logCat()
            disableAllList.forEach {
                repository.setAppState(it.packageName.toString())
            }
        }
        withContext(Dispatchers.IO) {
            uninstallResult.await()
            disableResult.await()
            disableAllResult.await()
        }
    }
}