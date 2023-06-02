package com.connor.fuckcolorapp.ui

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.connor.fuckcolorapp.R
import com.connor.fuckcolorapp.databinding.ActivityAppsBinding
import com.connor.fuckcolorapp.extension.*
import com.connor.fuckcolorapp.services.PackageService
import com.connor.fuckcolorapp.ui.adapter.TabPagerAdapter
import com.connor.fuckcolorapp.states.CheckError
import com.connor.fuckcolorapp.states.PureApp
import com.connor.fuckcolorapp.utils.subscribe
import com.connor.fuckcolorapp.viewmodels.AppsViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AppsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAppsBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<AppsViewModel>()

    private val tabPagerAdapter by lazy { TabPagerAdapter(this, supportFragmentManager, lifecycle) }
    private val imm by lazy { getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        initTab()
        initClick()
        initScope()
        initBackPressed()
    }

    private fun initBackPressed() {
        onBackPressedDispatcher.addCallback(this) {
            with(binding.editSearch) {
                if (!text.isNullOrEmpty()) {
                    setText(""); isVisible = false
                } else finish()
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun initScope() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    binding.editSearch.textChanges().debounce(700).collect {
                        viewModel.queryAll(it)
                    }
                }
                launch {
                    subscribe {
                        when (it) {
                            is CheckError -> showToast(it.msg)
                            is PureApp -> {
                                binding.fabPure.isEnabled = true
                                viewModel.getAppsList()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initClick() {
        binding.fabPure.setOnClickListener { view ->
            if (viewModel.hasCheck) {
                view.isEnabled = false
                startService<PackageService> { putExtra("is_select", true) }
            }
        }
        binding.imgSearch.setOnClickListener {
            with(binding.editSearch) {
                isVisible = !isVisible
                if (isVisible) {
                    requestFocus()
                    imm.showSoftInput(this, 0)
                } else {
                    imm.hideSoftInputFromWindow(windowToken, 0)
                }
            }
        }
    }

    private fun initTab() {
        binding.pageTab.adapter = tabPagerAdapter
        binding.pageTab.offscreenPageLimit = 3
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
                        1, 2 -> binding.fabPure.load(R.drawable.baseline_ac_unit_24)
                        else -> {}
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                with(binding.editSearch) {
                    if (!isVisible) onBackPressedDispatcher.onBackPressed()
                    else {
                        setText("")
                        isVisible = false
                        imm.hideSoftInputFromWindow(windowToken, 0)
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}