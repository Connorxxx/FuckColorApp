package com.connor.fuckcolorapp.models

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.IBinder
import android.os.ParcelFileDescriptor
import com.connor.core.emitEvent
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.BuildConfig
import com.connor.fuckcolorapp.consts.Consts
import com.connor.fuckcolorapp.consts.Consts.MATCH_UNINSTALLED
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.extension.showToast
import com.connor.fuckcolorapp.utils.TargetApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import moe.shizuku.server.IShizukuService
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuBinderWrapper
import rikka.shizuku.SystemServiceHelper
import javax.inject.Inject

class Repository @Inject constructor(@ApplicationContext val context: Context) {

    private val myUserId get() = android.os.Process.myUserHandle().hashCode()

    private val ResolveInfo.isSystemApp: Boolean
        get() = activityInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == ApplicationInfo.FLAG_SYSTEM

    private val pm: PackageManager = context.packageManager

    fun checkPermission() = kotlin.runCatching {
        when {
            Shizuku.isPreV11() -> false
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED -> true
            Shizuku.shouldShowRequestPermissionRationale() -> {
                false
            }
            else -> {
                Shizuku.requestPermission(0)
                false
            }
        }
    }.getOrDefault(false)

    fun disableAppWithCheck(packageName: String) = checkShizuku { disableApp(packageName) }

    fun disableApp(packageName: String): Boolean {
        getPackageInfoOrNull(packageName) ?: return false
        return kotlin.runCatching {
            asInterface("android.content.pm.IPackageManager", "package").also {
                it::class.java.getMethod(
                    "setApplicationEnabledSetting",
                    String::class.java,
                    Int::class.java,
                    Int::class.java,
                    Int::class.java,
                    String::class.java
                ).invoke(
                    it,
                    packageName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER,
                    0,
                    myUserId,
                    BuildConfig.APPLICATION_ID
                )
            }
            "Disable $packageName Success".logCat()
        }.isSuccess
    }

    fun uninstallAppWithCheck(packageName: String) = checkShizuku { uninstallApp(packageName) }
    private fun uninstallApp(packageName: String): Boolean {
        return kotlin.runCatching {
            IShizukuService.Stub.asInterface(Shizuku.getBinder())
                .newProcess(arrayOf("sh"), null, null).run {
                    ParcelFileDescriptor.AutoCloseOutputStream(outputStream).use {
                        it.write("pm uninstall --user current $packageName".toByteArray())
                    }
                    (waitFor() == 0).also {
                        destroy()
                        "uninstall $packageName Success".logCat()
                    }
                }
        }.getOrDefault(false)
    }

    suspend fun getUserAppList() = queryPackage().filter { !it.isSystemApp }.also { query ->
        App.userAppList.clear()
        query.forEach {
            App.userAppList.add(
                AppInfo(
                    it.loadLabel(pm),
                    it.activityInfo.packageName,
                    it.loadIcon(pm),
                    true
                )
            )
        }
        App.userAppList.sortBy { list -> list.label.toString() }
    }

    suspend fun getSystemAppList() = queryPackage(PackageManager.MATCH_SYSTEM_ONLY).also { query ->
        App.systemAppList.clear()
        query.forEach {
            App.systemAppList.add(
                AppInfo(
                    it.loadLabel(pm),
                    it.activityInfo.packageName,
                    it.loadIcon(pm),
                    true
                )
            )
        }
        App.systemAppList.sortBy { list -> list.label.toString() }
    }

    suspend fun queryPackageWithCheck() = checkShizuku { queryPackage() }
    suspend fun queryPackage(flags: Int = PackageManager.MATCH_ALL): MutableList<ResolveInfo> =
        withContext(Dispatchers.IO) {
            val i = Intent(Intent.ACTION_MAIN, null)
            i.addCategory(Intent.CATEGORY_LAUNCHER)
            if (TargetApi.T) {
                pm.queryIntentActivities(
                    i,
                    PackageManager.ResolveInfoFlags.of(flags.toLong())
                )
            } else {
                pm.queryIntentActivities(i, flags)
            }
        }


    private fun asInterface(className: String, serviceName: String): Any =
        ShizukuBinderWrapper(SystemServiceHelper.getSystemService(serviceName)).let {
            Class.forName("$className\$Stub").run {
                getMethod("asInterface", IBinder::class.java).invoke(null, it)
            }
        }

    private fun getPackageInfoOrNull(packageName: String, flags: Int = Consts.MATCH_UNINSTALLED) =
        kotlin.runCatching {
            if (TargetApi.T) context.packageManager.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(flags.toLong())
            )
            else context.packageManager.getPackageInfo(packageName, flags)
        }.getOrNull()


    inline fun <T> checkShizuku(block: () -> T): T? {
        checkPermission().also {
            if (!it) {
                emitEvent("Error Check Shizuku", Consts.CHECK_FALSE)
            }
            else return block()
        }
        return null
    }
}
