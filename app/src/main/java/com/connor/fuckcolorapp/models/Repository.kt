package com.connor.fuckcolorapp.models

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.IBinder
import android.os.ParcelFileDescriptor
import com.connor.core.emitEvent
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.BuildConfig
import com.connor.fuckcolorapp.R
import com.connor.fuckcolorapp.consts.Consts
import com.connor.fuckcolorapp.consts.Consts.MATCH_UNINSTALLED
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.utils.TargetApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moe.shizuku.server.IShizukuService
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuBinderWrapper
import rikka.shizuku.SystemServiceHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    @ApplicationContext val context: Context,
    val app: App
) {

    private val myUserId get() = android.os.Process.myUserHandle().hashCode()

    private val ResolveInfo.isSystemApp: Boolean
        get() = activityInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == ApplicationInfo.FLAG_SYSTEM

    private val pm: PackageManager by lazy { context.packageManager }

    fun checkPermission() = runCatching {
        when {
            Shizuku.isPreV11() -> false
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED -> true
            Shizuku.shouldShowRequestPermissionRationale() -> false
            else -> {
                Shizuku.requestPermission(0)
                false
            }
        }
    }.getOrElse { false }

    fun setAppState(packageName: String, isEnable: Boolean = false) =
        getPackageInfoOrNull(packageName)?.let {
            if (!isEnable) forceStopApp(packageName)
            runCatching {
                asInterface("android.content.pm.IPackageManager", "package").also {
                    val state =
                        if (isEnable) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
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
                        state,
                        0,
                        myUserId,
                        BuildConfig.APPLICATION_ID
                    )
                }
            }.isSuccess
        } ?: false


    fun uninstallApp(packageName: String) = getPackageInfoOrNull(packageName)?.let {
        runCatching {
            IShizukuService.Stub.asInterface(Shizuku.getBinder())
                .newProcess(arrayOf("sh"), null, null).run {
                    ParcelFileDescriptor.AutoCloseOutputStream(outputStream).use {
                        it.write("pm uninstall --user current $packageName".toByteArray())
                    }
                    (waitFor() == 0).also { destroy() }
                }
        }.getOrElse { false }  //Il0O
    } ?: false

    suspend fun getUserAppList(value: CharSequence = "") = queryPackage().filter { !it.isSystemApp }.also { query ->
        app.userAppList.clear()
        query.forEach {
            val label = it.loadLabel(pm)
            if (label.contains(value)) {
                app.userAppList.add(AppInfo(label, it.activityInfo.packageName, it.loadIcon(pm)))
            }
        }
        app.userAppList.sortBy { list -> list.label.toString() }
    }

    suspend fun getSystemAppList(value: CharSequence = "") = queryPackage(PackageManager.MATCH_SYSTEM_ONLY).also { query ->
        app.systemAppList.clear()
        query.forEach {
            val label = it.loadLabel(pm)
            if (label.contains(value)) {
                app.systemAppList.add(AppInfo(label, it.activityInfo.packageName, it.loadIcon(pm)))
            }
        }
        app.systemAppList.sortBy { list -> list.label.toString() }
    }

    suspend fun getAllAppList(value: CharSequence = "") = queryInstalledPackages().also { query ->
        app.allAppList.clear()
        query.forEach {
            val label = it.applicationInfo.loadLabel(pm)
            if (label.contains(value)) {
                app.allAppList.add(AppInfo(label, it.packageName, it.applicationInfo.loadIcon(pm)))
            }
        }
        app.allAppList.sortBy { list -> list.label.toString() }
    }

    suspend fun getDisableList(value: CharSequence = "") = queryInstalledPackages().also { query ->
        app.disableList.clear()
        query.forEach {
            val label = it.applicationInfo.loadLabel(pm)
            if (isDisabled(it.packageName)) {
                if (label.contains(value)) {
                    app.disableList.add(AppInfo(label, it.packageName, it.applicationInfo.loadIcon(pm)))
                }
            }
        }
        app.disableList.sortBy { list -> list.label.toString() }
    }

    private fun forceStopApp(packageName: String) = runCatching {
        asInterface("android.app.IActivityManager", "activity").let {
            it::class.java.getMethod(
                "forceStopPackage", String::class.java, Int::class.java
            ).invoke(
                it, packageName, myUserId
            )
        }
        true
    }.getOrElse { false }

private suspend fun queryPackage(flags: Int = MATCH_UNINSTALLED): MutableList<ResolveInfo> =
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

private suspend fun queryInstalledPackages(flags: Int = PackageManager.MATCH_ALL): List<PackageInfo> =
    withContext(Dispatchers.IO) {
        if (TargetApi.T) {
            pm.getInstalledPackages(
                PackageManager.PackageInfoFlags.of(
                    flags.toLong()
                )
            )
        } else pm.getInstalledPackages(flags)
    }


private fun asInterface(className: String, serviceName: String): Any =
    ShizukuBinderWrapper(SystemServiceHelper.getSystemService(serviceName)).let {
        Class.forName("$className\$Stub").run {
            getMethod("asInterface", IBinder::class.java).invoke(null, it)
        }
    }

private fun isDisabled(packageName: String) =
    getPackageInfoOrNull(packageName)?.applicationInfo?.enabled?.not() ?: false

private fun getPackageInfoOrNull(packageName: String, flags: Int = Consts.MATCH_UNINSTALLED) =
    runCatching {
        if (TargetApi.T) context.packageManager.getPackageInfo(
            packageName,
            PackageManager.PackageInfoFlags.of(flags.toLong())
        )
        else context.packageManager.getPackageInfo(packageName, flags)
    }.getOrNull()


inline fun <T> checkShizuku(block: () -> T): T? {
    checkPermission().also {
        if (!it && !BuildConfig.DEBUG) {
            emitEvent(context.getString(R.string.error), Consts.CHECK_FALSE)
        } else return block()
    }
    return null
}
}
