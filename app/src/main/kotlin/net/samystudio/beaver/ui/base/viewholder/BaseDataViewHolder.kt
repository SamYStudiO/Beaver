package net.samystudio.beaver.ui.base.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseDataViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    abstract var data: T
}