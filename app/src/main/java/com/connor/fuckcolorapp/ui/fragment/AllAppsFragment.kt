package com.connor.fuckcolorapp.ui.fragment

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.databinding.FragmentAllAppsBinding
import com.connor.fuckcolorapp.extension.repeatOnStart
import com.connor.fuckcolorapp.extension.viewBinding
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
class AllAppsFragment : BaseFragment<FragmentAllAppsBinding>() {

    @Inject lateinit var app: App
    @Inject lateinit var appListAdapter: AppListAdapter
    @Inject lateinit var headerAdapter: HeaderAdapter
    @Inject lateinit var footerAdapter: FooterAdapter

    private val viewModel by activityViewModels<AppsViewModel>()

    override val binding by viewBinding<FragmentAllAppsBinding>()

    override fun initData() {}

    override fun initView() {
        initUI()
        initScope()
    }

    private fun initUI() {

        with(binding.rvAll) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ConcatAdapter(headerAdapter, appListAdapter, footerAdapter)
        }
        appListAdapter.setClickListener { info ->
            app.allAppList.find {
                it.label == info.label
            }?.also {
                it.isCheck = !it.isCheck
            }
        }
        binding.swipeAll.setOnRefreshListener {
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
}