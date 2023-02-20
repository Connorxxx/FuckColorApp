package com.connor.fuckcolorapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class AppListAdapter @Inject constructor(@ActivityContext val context: Context) :
    ListAdapter<AppInfo, AppListAdapter.ViewHolder>(FlowerDiffCallback) {
    object FlowerDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem == newItem
        }
    }

    var listener: ((AppInfo) -> Unit?)? = null

    fun setClickListener(listener: (AppInfo) -> Unit) {
        this.listener = listener
    }

    inner class ViewHolder(private val binding: ItemAppInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cardApp.setOnClickListener {
                binding.m?.let {
                    Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:" + it.packageName)
                        context.startActivity(this)
                    }
                }
            }
            binding.checkBox.setOnClickListener {
                binding.m?.let { info ->
                    listener?.let { it(info) }
                    binding.cardApp.setCardBackgroundColor(
                        if (info.isCheck) context.getColor(R.color.primary)
                        else context.getColor(R.color.background)
                    )
                }
            }
        }

        fun bind(appInfo: AppInfo) {
            binding.m = appInfo
            binding.imgIcon.load(appInfo.icon)
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
        holder.bind(repo)
    }
}

