package com.connor.fuckcolorapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.connor.fuckcolorapp.R
import com.connor.fuckcolorapp.databinding.FooterItemBinding
import com.connor.fuckcolorapp.databinding.ItemDisableAppBinding
import com.connor.fuckcolorapp.extension.Inflater
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class FooterAdapter @Inject constructor() : RecyclerView.Adapter<FooterAdapter.ViewHolder>() {

    val inflater: Inflater<FooterItemBinding> get() = FooterItemBinding::inflate

    inner class ViewHolder(private val binding: FooterItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun getBinding(): FooterItemBinding {
            return binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount() = 1
}