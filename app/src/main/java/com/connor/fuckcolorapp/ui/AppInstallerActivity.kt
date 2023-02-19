package com.connor.fuckcolorapp.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import com.connor.fuckcolorapp.databinding.ActivityAppInstallerBinding
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.extension.showToast
import java.io.File

class AppInstallerActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAppInstallerBinding.inflate(layoutInflater) }

    private val file by lazy { File(filesDir, "test.apk") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        intent?.data?.let {
            contentResolver.openInputStream(it).use { input ->
                file.outputStream().use { out ->
                    input?.copyTo(out)
                }
            }
            file.absolutePath.logCat()
        }
    }
}