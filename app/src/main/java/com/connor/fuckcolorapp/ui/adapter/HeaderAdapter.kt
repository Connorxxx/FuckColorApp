package com.connor.fuckcolorapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.connor.fuckcolorapp.databinding.HeaderItemBinding
import com.connor.fuckcolorapp.extension.Inflater
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class HeaderAdapter @Inject constructor() : RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {


    val inflater: Inflater<HeaderItemBinding> get() = HeaderItemBinding::inflate
    inner class ViewHolder(private val binding: HeaderItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun getBinding(): HeaderItemBinding {
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