package net.samystudio.beaver.ui.base.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.ButterKnife

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    init
    {
        @Suppress("LeakingThis")
        ButterKnife.bind(this, itemView)
    }
}