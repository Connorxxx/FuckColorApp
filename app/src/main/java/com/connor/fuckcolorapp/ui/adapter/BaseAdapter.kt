package com.connor.fuckcolorapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.connor.fuckcolorapp.extension.Inflater

abstract class BaseAdapter<T : Any, VB : ViewBinding>(
    private val itemTheSame: (T, T) -> Boolean,
    private val contentsTheSame: (T, T) -> Boolean,
    val inflater: Inflater<VB>
) : ListAdapter<T, BaseAdapter<T, VB>.BaseViewHolder>(object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = itemTheSame(oldItem, newItem)
    override fun areContentsTheSame(oldItem: T, newItem: T) = contentsTheSame(oldItem, newItem)
}) {

    protected var listener: ((T) -> Unit)? = null

    fun setClickListener(l: (T) -> Unit) {
        listener = l
    }

    //protected abstract val inflater: Inflater<VB>

    protected abstract fun getViewHolder(binding: VB): BaseViewHolder

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
//            listener?.let {
//                holder.itemView.setOnClickListener { it(model) }
//            }
        }
    }
}