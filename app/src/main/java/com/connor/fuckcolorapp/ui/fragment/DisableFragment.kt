package com.connor.fuckcolorapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.databinding.FragmentDisableBinding
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.extension.repeatOnLifecycle
import com.connor.fuckcolorapp.states.AppLoad
import com.connor.fuckcolorapp.states.onAll
import com.connor.fuckcolorapp.states.onDisableLoaded
import com.connor.fuckcolorapp.ui.adapter.DisableListAdapter
import com.connor.fuckcolorapp.viewmodels.AppsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DisableFragment : Fragment() {

    @Inject lateinit var app: App
    @Inject lateinit var disableListAdapter: DisableListAdapter

    private val viewModel by activityViewModels<AppsViewModel>()

    private var _binding: FragmentDisableBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDisableBinding.inflate(inflater, container, false)
        initUI()
        initScope()
        return binding.root
    }

    private fun initUI() {
        with(binding.rvDisable) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = disableListAdapter
        }
        disableListAdapter.setClickListener { info ->
            viewModel.setAppEnable(info.packageName.toString())
            viewModel.loadDisable()
            viewModel.loadUser()
            viewModel.loadSystem()
        }
        binding.swipeDisable.setOnRefreshListener {
            viewModel.loadDisable()
        }
    }

    private fun initScope() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}