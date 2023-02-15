package com.connor.fuckcolorapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.connor.core.receiveEvent
import com.connor.fuckcolorapp.databinding.ActivityMainBinding
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.extension.showToast
import com.connor.fuckcolorapp.extension.startService
import com.connor.fuckcolorapp.services.PackageService
import com.connor.fuckcolorapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        binding.btnUninstall.setOnClickListener {
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