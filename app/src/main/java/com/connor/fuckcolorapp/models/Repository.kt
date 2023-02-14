package com.connor.fuckcolorapp.models

import android.content.Context
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.ParcelFileDescriptor
import com.connor.fuckcolorapp.BuildConfig
import com.connor.fuckcolorapp.consts.Consts
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.utils.TargetApi
import dagger.hilt.android.qualifiers.ApplicationContext
import moe.shizuku.server.IShizukuService
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuBinderWrapper
import rikka.shizuku.SystemServiceHelper
import javax.inject.Inject

class Repository @Inject constructor(@ApplicationContext val context: Context) {

    private val myUserId get() = android.os.Process.myUserHandle().hashCode()

    fun checkPermission() = when {
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

    fun disableApp(packageName: String) {
        kotlin.runCatching {
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
        }.onFailure {
            "ERROR: ${it.message}".logCat()
        }
    }

    fun uninstallApp(packageName: String) {
        kotlin.runCatching {
            IShizukuService.Stub.asInterface(Shizuku.getBinder())
                .newProcess(arrayOf("sh"), null, null).run {
                    ParcelFileDescriptor.AutoCloseOutputStream(outputStream).use {
                        it.write("pm uninstall --user current $packageName".toByteArray())
                    }
                    (waitFor() == 0 ).also {
                        destroy()
                        "uninstall $packageName Success".logCat()
                    }
                }
        }.onFailure {
            "ERROR: ${it.message}".logCat()
        }
    }

    private fun asInterface(className: String, serviceName: String): Any =
        ShizukuBinderWrapper(SystemServiceHelper.getSystemService(serviceName)).let {
            Class.forName("$className\$Stub").run {
                getMethod("asInterface", IBinder::class.java).invoke(null, it)
            }
        }

    private fun getPackageInfo(packageName: String, flags: Int = Consts.MATCH_UNINSTALLED) =
        kotlin.runCatching {
            if (TargetApi.T) context.packageManager.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(flags.toLong())
            )
            else context.packageManager.getPackageInfo(packageName, flags)
        }
}