package com.connor.fuckcolorapp.ui

import android.os.Bundle
import android.view.View
import android.widget.TableLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import coil.load
import com.connor.core.receiveEvent
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.R
import com.connor.fuckcolorapp.consts.Consts
import com.connor.fuckcolorapp.databinding.ActivityAppsBinding
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.extension.showSnackbar
import com.connor.fuckcolorapp.extension.showToast
import com.connor.fuckcolorapp.extension.startService
import com.connor.fuckcolorapp.services.PackageService
import com.connor.fuckcolorapp.ui.adapter.TabPagerAdapter
import com.connor.fuckcolorapp.viewmodels.AppsViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAppsBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<AppsViewModel>()

    private val tabPagerAdapter by lazy {
        TabPagerAdapter(this, supportFragmentManager, lifecycle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.pageTab.adapter = tabPagerAdapter
        binding.pageTab.offscreenPageLimit = 2
        TabLayoutMediator(binding.tab, binding.pageTab) { tab, position ->
            tab.text = tabPagerAdapter.getPageTitle(position)

        }.attach()
        binding.tab.selectedTabPosition
        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    binding.fabPure.isVisible = it.position != 3
                    when (it.position) {
                        0 -> binding.fabPure.load(R.drawable.delete)
                        1 , 2 -> binding.fabPure.load(R.drawable.baseline_ac_unit_24)
                        else -> {}
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        binding.fabPure.setOnClickListener { view ->
            if (viewModel.hasCheck) {
                view.isEnabled = false
                viewModel.setLoading()
                startService<PackageService> {
                    putExtra("is_select", true)
                }
            }
        }
        receiveEvent<String>(Consts.PURE_APP) {
            binding.fabPure.isEnabled = true
            viewModel.getAppsList()
        }
        receiveEvent<String>(Consts.CHECK_FALSE) {
            showToast(it)
        }
    }
}