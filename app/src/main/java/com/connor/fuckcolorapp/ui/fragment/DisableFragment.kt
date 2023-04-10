package com.connor.fuckcolorapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.databinding.FragmentDisableBinding
import com.connor.fuckcolorapp.extension.Inflater
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.extension.repeatOnStart
import com.connor.fuckcolorapp.states.onAll
import com.connor.fuckcolorapp.states.onDisableLoaded
import com.connor.fuckcolorapp.ui.adapter.DisableListAdapter
import com.connor.fuckcolorapp.viewmodels.AppsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DisableFragment : BaseFragment<FragmentDisableBinding>() {

    @Inject
    lateinit var app: App
    @Inject
    lateinit var disableListAdapter: DisableListAdapter

    private val viewModel by activityViewModels<AppsViewModel>()

    override val inflater: Inflater<FragmentDisableBinding> get() = FragmentDisableBinding::inflate

    override fun initData() {}
    override fun initView() {
        initUI()
        initScope()
    }


    private fun initUI() {
        with(binding.rvDisable) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = disableListAdapter
        }
        disableListAdapter.setClickListener { info ->
            with(viewModel) {
                setAppEnable(info.packageName.toString())
                loadDisable()
                loadUser()
                loadSystem()
            }
        }
        binding.swipeDisable.setOnRefreshListener {
            viewModel.loadDisable()
        }
    }

    private fun initScope() {
        repeatOnStart {
            launch {
                viewModel.listEvent.collect {
                    binding.progressDisable.isVisible = false
                    it.onDisableLoaded {
                        binding.swipeDisable.isRefreshing = false
                        disableListAdapter.submitList(ArrayList(app.disableList))
                    }
                }
            }
            launch {
                viewModel.listState.collect {
                    binding.progressDisable.isVisible = false
                    it.onAll {
                        disableListAdapter.submitList(ArrayList(app.disableList))
                        binding.swipeDisable.isRefreshing = false   //bypass
                    }
                }
            }
        }
    }
}