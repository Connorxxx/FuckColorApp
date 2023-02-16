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
import androidx.recyclerview.widget.LinearLayoutManager
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.databinding.FragmentSystemAppBinding
import com.connor.fuckcolorapp.states.AppLoad
import com.connor.fuckcolorapp.ui.adapter.AppListAdapter
import com.connor.fuckcolorapp.viewmodels.AppsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SystemAppFragment : Fragment() {

    private val viewModel by activityViewModels<AppsViewModel>()

    private var _binding: FragmentSystemAppBinding? = null
    private val binding get() = _binding!!

    private val appListAdapter by lazy {
        AppListAdapter { info ->
            App.systemAppList.find {
                it.label == info.label
            }.also {
                it!!.isCheck = !it.isCheck
            }
          //  viewModel.uploadSystem()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSystemAppBinding.inflate(inflater, container, false)
        with(binding.rvSystem) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = appListAdapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.systemListState.collect {
                    binding.progressSystem.isVisible = it == AppLoad.Loading
                    when (it) {
                        is AppLoad.SystemLoaded -> {
                            appListAdapter.submitList(App.systemAppList)
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