package com.connor.fuckcolorapp.ui.fragment

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.databinding.FragmentUserAppBinding
import com.connor.fuckcolorapp.extension.Inflater
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.extension.repeatOnStart
import com.connor.fuckcolorapp.extension.viewBinding
import com.connor.fuckcolorapp.states.onAll
import com.connor.fuckcolorapp.states.onUserLoaded
import com.connor.fuckcolorapp.ui.adapter.AppListAdapter
import com.connor.fuckcolorapp.ui.adapter.FooterAdapter
import com.connor.fuckcolorapp.ui.adapter.HeaderAdapter
import com.connor.fuckcolorapp.viewmodels.AppsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserAppFragment : BaseFragment<FragmentUserAppBinding>() {

    @Inject lateinit var app: App
    @Inject lateinit var appListAdapter: AppListAdapter
    @Inject lateinit var headerAdapter: HeaderAdapter
    @Inject lateinit var footerAdapter: FooterAdapter

    private val viewModel by activityViewModels<AppsViewModel>()

    override val binding: FragmentUserAppBinding by viewBinding()

    override fun initData() {}

    override fun initView() {
        initUI()
        initScope()
    }

    private fun initUI() {
        with(binding.rvUser) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ConcatAdapter(headerAdapter, appListAdapter, footerAdapter)
        }
        appListAdapter.setClickListener { info ->
            info.logCat()
            app.userAppList.find {
                it.label == info.label
            }?.also {
                it.isCheck = !it.isCheck
            }
        }
        binding.swipeUser.setOnRefreshListener {
            viewModel.loadUser()
        }
    }

    private fun initScope() {
        repeatOnStart {
            launch {
                viewModel.listEvent.collect {
                    it.onUserLoaded {
                        binding.progressUser.isVisible = false
                        appListAdapter.submitList(ArrayList(app.userAppList))
                        binding.swipeUser.isRefreshing = false
                    }
                }
            }
            launch {
                viewModel.listState.collect {
                    it.onAll {
                        binding.progressUser.isVisible = false
                        appListAdapter.submitList(ArrayList(app.userAppList))
                        binding.swipeUser.isRefreshing = false
                    }
                }
            }
        }
    }
}