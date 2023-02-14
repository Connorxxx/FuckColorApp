package com.connor.fuckcolorapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.connor.fuckcolorapp.databinding.ActivityMainBinding
import com.connor.fuckcolorapp.viewmodels.MainViewModel
import com.connor.fuckcolorapp.work.PackageWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val request by lazy {
        OneTimeWorkRequestBuilder<PackageWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
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
            if (viewModel.permissionState.value)
                WorkManager.getInstance(this).enqueue(request);
        }
    }
}