package net.samystudio.beaver.ui.base.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.ButterKnife
import kotlinx.android.extensions.LayoutContainer

class BaseViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
                                                         LayoutContainer
{
    init
    {
        ButterKnife.bind(this, itemView)
    }
}