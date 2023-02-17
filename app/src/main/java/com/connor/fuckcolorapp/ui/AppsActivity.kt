package com.connor.fuckcolorapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.connor.core.receiveEvent
import com.connor.fuckcolorapp.R
import com.connor.fuckcolorapp.consts.Consts
import com.connor.fuckcolorapp.databinding.ActivityAppsBinding
import com.connor.fuckcolorapp.extension.showToast
import com.connor.fuckcolorapp.extension.startService
import com.connor.fuckcolorapp.services.PackageService
import com.connor.fuckcolorapp.ui.adapter.TabPagerAdapter
import com.connor.fuckcolorapp.viewmodels.AppsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAppsBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<AppsViewModel>()

    private val tabPagerAdapter by lazy {
        TabPagerAdapter(
            supportFragmentManager,
            lifecycle,
            viewModel.fragments,
            viewModel.titles
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.pageTab.adapter = tabPagerAdapter
        binding.pageTab.offscreenPageLimit = 2
        TabLayoutMediator(binding.tab, binding.pageTab) { tab, position ->
            tab.text = viewModel.titles[position]
        }.attach()
        binding.fabPure.setOnClickListener {
            it.isEnabled = false
            binding.fabPure.load(R.drawable.hourglass_full)
            viewModel.setLoading()
            startService<PackageService> {
                putExtra("is_select", true)
            }
        }
        receiveEvent<String>(Consts.PURE_APP) {
            binding.fabPure.isEnabled = true
            binding.fabPure.load(R.drawable.baseline_ac_unit_24)
            viewModel.getAppsList()
        }
        receiveEvent<String>(Consts.CHECK_FALSE) {
            it.showToast()
        }
    }
}