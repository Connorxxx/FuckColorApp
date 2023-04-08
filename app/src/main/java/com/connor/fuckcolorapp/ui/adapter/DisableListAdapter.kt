package com.connor.fuckcolorapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.connor.fuckcolorapp.R
import com.connor.fuckcolorapp.databinding.ItemDisableAppBinding
import com.connor.fuckcolorapp.models.AppInfo
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class DisableListAdapter @Inject constructor(@ActivityContext val context: Context) :
    BaseAdapter<AppInfo, ItemDisableAppBinding>(FlowerDiffCallback) {

    inner class DisableHolder(binding: ItemDisableAppBinding) : BaseViewHolder(binding) {
        init {
            binding.cardApp.setOnClickListener {
                binding.m?.let {
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:" + it.packageName)
                        context.startActivity(this)
                    }
                }
            }
            binding.cardEnable.setOnClickListener {
                binding.m?.let { info ->
                    listener?.let { it(info) }
                }
            }
        }

        override fun bind(data: AppInfo) {
            binding.m = data
            binding.imgIcon.load(data.icon)
        }
    }

    override val inflater: (LayoutInflater, ViewGroup?, Boolean) -> ItemDisableAppBinding
        get() = ItemDisableAppBinding::inflate

    override fun getViewHolder(binding: ItemDisableAppBinding) = DisableHolder(binding)
}