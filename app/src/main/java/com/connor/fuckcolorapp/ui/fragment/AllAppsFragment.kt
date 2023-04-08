package com.connor.fuckcolorapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.databinding.FragmentAllAppsBinding
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.extension.repeatOnStart
import com.connor.fuckcolorapp.states.onAll
import com.connor.fuckcolorapp.states.onAllLoaded
import com.connor.fuckcolorapp.ui.adapter.AppListAdapter
import com.connor.fuckcolorapp.ui.adapter.FooterAdapter
import com.connor.fuckcolorapp.ui.adapter.HeaderAdapter
import com.connor.fuckcolorapp.viewmodels.AppsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AllAppsFragment : Fragment() {

    @Inject
    lateinit var app: App

    @Inject
    lateinit var appListAdapter: AppListAdapter

    @Inject
    lateinit var headerAdapter: HeaderAdapter

    @Inject
    lateinit var footerAdapter: FooterAdapter

    private val viewModel by activityViewModels<AppsViewModel>()

    private var _binding: FragmentAllAppsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllAppsBinding.inflate(inflater, container, false)
        initUI()
        initScope()
        return binding.root
    }

    private fun initUI() {
        with(binding.rvAll) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ConcatAdapter(headerAdapter, appListAdapter, footerAdapter)
        }
        appListAdapter.setClickListener { info ->
            info.logCat()
            app.allAppList.find {
                it.label == info.label
            }?.also {
                it.isCheck = !it.isCheck
            }
        }
        binding.swipeAll.setOnRefreshListener {
            //   viewModel.setAllAppsLoading()
            viewModel.loadAll()
        }
    }

    private fun initScope() {
        repeatOnStart {
            launch {
                viewModel.listEvent.collect {
                    binding.progressAll.isVisible = false
                    it.onAllLoaded {
                        binding.swipeAll.isRefreshing = false
                        appListAdapter.submitList(ArrayList(app.allAppList))
                    }
                }
            }
            launch {
                viewModel.listState.collect {
                    binding.progressAll.isVisible = false
                    it.onAll {
                        appListAdapter.submitList(ArrayList(app.allAppList))
                        binding.swipeAll.isRefreshing = false
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