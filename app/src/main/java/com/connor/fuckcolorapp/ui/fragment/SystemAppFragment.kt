package com.connor.fuckcolorapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.databinding.FragmentSystemAppBinding
import com.connor.fuckcolorapp.extension.repeatOnLifecycle
import com.connor.fuckcolorapp.states.AppLoad
import com.connor.fuckcolorapp.states.onAll
import com.connor.fuckcolorapp.states.onSystemLoaded
import com.connor.fuckcolorapp.ui.adapter.AppListAdapter
import com.connor.fuckcolorapp.ui.adapter.FooterAdapter
import com.connor.fuckcolorapp.ui.adapter.HeaderAdapter
import com.connor.fuckcolorapp.viewmodels.AppsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SystemAppFragment : Fragment() {

    @Inject lateinit var app: App
    @Inject lateinit var appListAdapter: AppListAdapter
    @Inject lateinit var headerAdapter: HeaderAdapter
    @Inject lateinit var footerAdapter: FooterAdapter

    private val viewModel by activityViewModels<AppsViewModel>()

    private var _binding: FragmentSystemAppBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSystemAppBinding.inflate(inflater, container, false)
        initUI()
        initScope()
        return binding.root
    }

    private fun initUI() {
        with(binding.rvSystem) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ConcatAdapter(headerAdapter, appListAdapter, footerAdapter)
        }
        appListAdapter.setClickListener { info ->
            app.systemAppList.find {
                it.label == info.label
            }?.also {
                it.isCheck = !it.isCheck
            }
        }
        binding.swipeSystem.setOnRefreshListener {
            viewModel.loadSystem()
        }
    }

    private fun initScope() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.listEvent.collect {
                        binding.progressSystem.isVisible = false
                        it.onSystemLoaded {
                            binding.swipeSystem.isRefreshing = false
                            appListAdapter.submitList(ArrayList(app.systemAppList))
                        }
                    }
                }
                launch {
                    viewModel.listState.collect {
                        binding.progressSystem.isVisible = false
                        it.onAll {
                            appListAdapter.submitList(ArrayList(app.systemAppList))
                            binding.swipeSystem.isRefreshing = false
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