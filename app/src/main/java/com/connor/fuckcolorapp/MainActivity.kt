package com.connor.fuckcolorapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.connor.core.receiveEvent
import com.connor.fuckcolorapp.databinding.ActivityMainBinding
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.extension.showToast
import com.connor.fuckcolorapp.extension.startService
import com.connor.fuckcolorapp.services.PackageService
import com.connor.fuckcolorapp.viewmodels.MainViewModel
import com.connor.fuckcolorapp.work.PackageWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val request by lazy {
        OneTimeWorkRequestBuilder<PackageWorker>()
            .setExpedited(OutOfQuotaPolicy.DROP_WORK_REQUEST)
            .setInputData(workDataOf("PACKAGE_NAME" to "com.connor.launcher"))
            .build()
    }

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnTest.setOnClickListener {
            viewModel.disableApp("com.connor.launcher")
        }
        binding.btnUninstall.setOnClickListener {
           // WorkManager.getInstance(this).enqueue(request)
            startService<PackageService> {}
        }
        binding.btnList.setOnClickListener {
            viewModel.queryPackage()
        }
        lifecycleScope.launch {
            launch {
                receiveEvent<String>("checkFalse") {
                    it.logCat()
                    it.showToast()
                }
            }
        }
    }
}