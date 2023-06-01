package com.connor.fuckcolorapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.connor.fuckcolorapp.extension.Inflater

abstract class BaseFragment<out VB : ViewBinding> : Fragment() {

    protected abstract val binding: VB

    protected abstract fun initView()
    protected abstract fun initData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
}