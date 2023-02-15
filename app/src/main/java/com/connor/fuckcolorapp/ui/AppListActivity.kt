package com.connor.fuckcolorapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.connor.fuckcolorapp.databinding.ActivityAppListBinding
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.states.AppLoad
import com.connor.fuckcolorapp.ui.adapter.AppListAdapter
import com.connor.fuckcolorapp.viewmodels.AppListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppListActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAppListBinding.inflate(layoutInflater) }

    private lateinit var appListAdapter: AppListAdapter

    private val viewModel by viewModels<AppListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initScope()
        with(binding.rvApp){
            val rvLayoutManager = LinearLayoutManager(this@AppListActivity)
            layoutManager = rvLayoutManager
            appListAdapter = AppListAdapter {  }
            adapter = appListAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initScope() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.appListState.collect {
                    when (it) {
                        is AppLoad.Loading -> {}
                        is AppLoad.Loaded -> {
                            appListAdapter.submitList(viewModel.appList)
                            viewModel.appList.size.logCat()
                        }
                    }
                }
            }
        }
    }
}