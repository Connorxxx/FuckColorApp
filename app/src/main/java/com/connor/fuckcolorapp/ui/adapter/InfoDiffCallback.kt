package com.connor.fuckcolorapp.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.connor.fuckcolorapp.models.AppInfo

object InfoDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
    override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
        return oldItem.packageName == newItem.packageName
    }

    override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
        return oldItem == newItem
    }
}