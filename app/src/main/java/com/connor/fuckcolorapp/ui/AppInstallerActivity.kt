package com.connor.fuckcolorapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.connor.fuckcolorapp.databinding.ActivityAppInstallerBinding
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
        }
    }
}