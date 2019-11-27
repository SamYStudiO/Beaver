package net.samystudio.beaver.ui.base.adapter

import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class BaseAdapter<D, VH : BaseViewHolder<D>>(val items: List<D> = ArrayList()) :
    RecyclerView.Adapter<VH>() {
    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.data = items[position]
    }

    fun getItemAt(position: Int) = items[position]
}