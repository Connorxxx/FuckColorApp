package com.connor.fuckcolorapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.connor.core.receiveEvent
import com.connor.fuckcolorapp.consts.Consts
import com.connor.fuckcolorapp.databinding.ActivityMainBinding
import com.connor.fuckcolorapp.extension.showToast
import com.connor.fuckcolorapp.extension.startActivity
import com.connor.fuckcolorapp.extension.startService
import com.connor.fuckcolorapp.services.PackageService
import com.connor.fuckcolorapp.ui.AppsActivity
import com.connor.fuckcolorapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<MainViewModel>()

    private val shizukuPackage by lazy { "moe.shizuku.privileged.api" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.cardStart.setOnClickListener {
            viewModel.checkShizuku {
                startService<PackageService> {
                    putExtra("is_select", false)
                }
            }
        }
        binding.cardApps.setOnClickListener {
            viewModel.checkShizuku {
                startActivity<AppsActivity> { }
            }
        }
        binding.layoutShizuku.setOnClickListener {
            packageManager.getLaunchIntentForPackage(shizukuPackage)
                ?.let { startActivity(it) }
                ?: startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$shizukuPackage")
                    )
                )
        }
        binding.layoutHelp.setOnClickListener {
            //HelpDialog().show(supportFragmentManager, HelpDialog.TAG)
        }
        receiveEvent<String>(Consts.CHECK_FALSE) {
            it.showToast(this@MainActivity)
        }
    }
}