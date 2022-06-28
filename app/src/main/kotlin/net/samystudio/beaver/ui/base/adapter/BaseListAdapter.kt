@file:Suppress("unused")

package net.samystudio.beaver.ui.base.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseListAdapter<D : Any, VH : BaseViewHolder<D>>(
    diffUtilCallback: DiffUtil.ItemCallback<D> = object : DiffUtil.ItemCallback<D>() {
        override fun areItemsTheSame(oldItem: D, newItem: D) = false
        override fun areContentsTheSame(oldItem: D, newItem: D) = false
    },
) : ListAdapter<D, VH>(diffUtilCallback) {
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.data = getItem(position)
    }
}
