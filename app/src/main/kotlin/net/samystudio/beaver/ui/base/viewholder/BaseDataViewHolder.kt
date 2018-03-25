package net.samystudio.beaver.ui.base.viewholder

import android.view.View

abstract class BaseDataViewHolder<T>(itemView: View) : BaseViewHolder(itemView)
{
    abstract var data: T
}