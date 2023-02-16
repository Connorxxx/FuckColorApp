package com.connor.fuckcolorapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.connor.fuckcolorapp.databinding.HeaderItemBinding

class HeaderAdapter : RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: HeaderItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun getBinding(): HeaderItemBinding {
            return binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: HeaderItemBinding = HeaderItemBinding.inflate(
            LayoutInflater.from(parent.context),
            //R.layout.footer_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount() = 1
}