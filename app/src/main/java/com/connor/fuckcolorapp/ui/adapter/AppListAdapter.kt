package com.connor.fuckcolorapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.R
import com.connor.fuckcolorapp.databinding.ItemAppInfoBinding
import com.connor.fuckcolorapp.models.AppInfo

class AppListAdapter (private val onClick: (AppInfo) -> Unit) :
    ListAdapter<AppInfo, AppListAdapter.ViewHolder>(FlowerDiffCallback) {
    object FlowerDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem.label == newItem.label
        }

        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(private val binding: ItemAppInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.checkBox.setOnClickListener {
                binding.m?.let {
                    onClick(it)
                }
            }
        }

        fun bind(appInfo: AppInfo) {
            binding.m = appInfo
            binding.imgIcon.load(appInfo.icon)
//            binding.cardApp.setCardBackgroundColor(
//                if (appInfo.isCheck) App.app.getColor(R.color.primary) else App.app.getColor(
//                    R.color.background
//                )
//            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListAdapter.ViewHolder {
        val binding: ItemAppInfoBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_app_info,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppListAdapter.ViewHolder, position: Int) {
        val repo = getItem(position)
        repo?.let { holder.bind(it) }
    }

}