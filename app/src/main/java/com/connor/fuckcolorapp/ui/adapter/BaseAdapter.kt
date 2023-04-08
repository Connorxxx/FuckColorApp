package com.connor.fuckcolorapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.connor.fuckcolorapp.models.AppInfo

abstract class BaseAdapter<T: Any, VB: ViewBinding>(config: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, BaseAdapter<T, VB>.BaseViewHolder>(config) {

    var listener: ((AppInfo) -> Unit)? = null

    fun setClickListener(l: (AppInfo) -> Unit) {
        listener = l
    }

    abstract val inflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    abstract fun getViewHolder(binding: VB): BaseViewHolder

    abstract inner class BaseViewHolder(protected val binding: VB) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(data: T)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return getViewHolder(inflater(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position)?.let { model ->
            holder.bind(model)
        }
    }
}