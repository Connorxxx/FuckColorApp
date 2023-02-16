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
import com.connor.fuckcolorapp.extension.showToast
import com.connor.fuckcolorapp.models.AppInfo
import com.connor.fuckcolorapp.states.AppLoad
import com.connor.fuckcolorapp.ui.adapter.AppListAdapter
import com.connor.fuckcolorapp.ui.adapter.FooterAdapter
import com.connor.fuckcolorapp.ui.adapter.HeaderAdapter
import com.connor.fuckcolorapp.viewmodels.AppsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserAppFragment : Fragment() {

    private val viewModel by activityViewModels<AppsViewModel>()

    private var _binding: FragmentUserAppBinding? = null
    private val binding get() = _binding!!

    private val appListAdapter by lazy {
        AppListAdapter { info ->
            App.userAppList.find {
                it.label == info.label
            }.also {
                it!!.isCheck = !it.isCheck
            }
            //viewModel.uploadUser()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserAppBinding.inflate(inflater, container, false)
        with(binding.rvUser) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ConcatAdapter(HeaderAdapter(), appListAdapter, FooterAdapter())
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.appListState.collect {
                    binding.progressUser.isVisible = it == AppLoad.Loading
                    when (it) {
                        is AppLoad.UserLoaded -> {
                            appListAdapter.submitList(App.userAppList)
                        }
                        else -> {}
                    }
                }
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}