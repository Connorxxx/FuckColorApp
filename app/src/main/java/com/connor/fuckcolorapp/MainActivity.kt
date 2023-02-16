package com.connor.fuckcolorapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.connor.core.receiveEvent
import com.connor.fuckcolorapp.consts.Consts
import com.connor.fuckcolorapp.databinding.ActivityMainBinding
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.extension.showToast
import com.connor.fuckcolorapp.extension.startActivity
import com.connor.fuckcolorapp.extension.startService
import com.connor.fuckcolorapp.services.PackageService
import com.connor.fuckcolorapp.ui.AppsActivity
import com.connor.fuckcolorapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<MainViewModel>()

    private val shizukuPackage by lazy { "moe.shizuku.privileged.api" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnTest.setOnClickListener {
            // viewModel.disableApp("com.connor.launcher")
        }
        binding.btnUninstall.setOnClickListener {
            startService<PackageService> {
                putExtra("uninstall_package", "com.connor.launcher")
                putExtra("disable_package", "com.connor.moviecat")
            }
        }
        binding.btnList.setOnClickListener {
            viewModel.queryPackage()
        }
        binding.btnOpen.setOnClickListener {
            viewModel.checkShizuku {
                startActivity<AppsActivity> { }
            }
        }
        binding.btnShizuku.setOnClickListener {
            packageManager.getLaunchIntentForPackage(shizukuPackage)
                ?.let { startActivity(it) }
                ?: startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$shizukuPackage")
                    )
                )
        }
        receiveEvent<String>(Consts.CHECK_FALSE) {
            it.showToast()
        }
    }
}