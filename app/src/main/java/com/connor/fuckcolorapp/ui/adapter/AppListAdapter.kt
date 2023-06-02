package com.connor.fuckcolorapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import coil.load
import com.connor.fuckcolorapp.R
import com.connor.fuckcolorapp.databinding.ItemAppInfoBinding
import com.connor.fuckcolorapp.models.AppInfo
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class AppListAdapter @Inject constructor(@ActivityContext private val context: Context) :
    BaseAdapter<AppInfo, ItemAppInfoBinding>(
        itemTheSame = { oldItem, newItem -> oldItem.packageName == newItem.packageName },
        contentsTheSame = { oldItem, newItem -> oldItem == newItem },
        inflater = ItemAppInfoBinding::inflate
    ) {

    //override val inflater: Inflater<ItemAppInfoBinding> get() = ItemAppInfoBinding::inflate

    override fun getViewHolder(binding: ItemAppInfoBinding) = ViewHolder(binding)

    inner class ViewHolder(binding: ItemAppInfoBinding) : BaseViewHolder(binding) {
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

        override fun bind(data: AppInfo) {
            binding.m = data
            binding.imgIcon.load(data.icon)
        }
    }
}

