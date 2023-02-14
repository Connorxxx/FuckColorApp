package com.connor.fuckcolorapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.connor.fuckcolorapp.databinding.ActivityMainBinding
import com.connor.fuckcolorapp.viewmodels.MainViewModel
import com.connor.fuckcolorapp.work.PackageWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnTest.setOnClickListener {
            viewModel.disableApp("com.connor.launcher")
        }
        val request = OneTimeWorkRequestBuilder<PackageWorker>().setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST).build()

        binding.btnUninstall.setOnClickListener {
           // viewModel.uninstallApp("com.connor.launcher")
            WorkManager.getInstance(this).enqueue(request)
        }
    }
}