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
import com.connor.fuckcolorapp.databinding.FragmentUserAppBinding
import com.connor.fuckcolorapp.extension.repeatOnLifecycle
import com.connor.fuckcolorapp.states.AppLoad
import com.connor.fuckcolorapp.states.onAll
import com.connor.fuckcolorapp.states.onUserLoaded
import com.connor.fuckcolorapp.ui.adapter.AppListAdapter
import com.connor.fuckcolorapp.ui.adapter.FooterAdapter
import com.connor.fuckcolorapp.ui.adapter.HeaderAdapter
import com.connor.fuckcolorapp.viewmodels.AppsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserAppFragment : Fragment() {

    @Inject lateinit var app: App
    @Inject lateinit var appListAdapter: AppListAdapter
    @Inject lateinit var headerAdapter: HeaderAdapter
    @Inject lateinit var footerAdapter: FooterAdapter

    private val viewModel by activityViewModels<AppsViewModel>()

    private var _binding: FragmentUserAppBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserAppBinding.inflate(inflater, container, false)
        initUI()
        initScope()
        return binding.root
    }

    private fun initUI() {
        with(binding.rvUser) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ConcatAdapter(headerAdapter, appListAdapter, footerAdapter)
        }
        appListAdapter.setClickListener { info ->
            app.userAppList.find {
                it.label == info.label
            }?.also {
                it.isCheck = !it.isCheck
            }
        }
        binding.swipeUser.setOnRefreshListener {
         //   viewModel.setUserLoading()
            viewModel.loadUser()
        }
    }

    private fun initScope() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}