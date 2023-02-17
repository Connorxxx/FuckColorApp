package com.connor.fuckcolorapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.connor.fuckcolorapp.R
import com.connor.fuckcolorapp.databinding.FooterItemBinding
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class FooterAdapter @Inject constructor() : RecyclerView.Adapter<FooterAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: FooterItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun getBinding(): FooterItemBinding {
            return binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FooterItemBinding = FooterItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount() = 1
}