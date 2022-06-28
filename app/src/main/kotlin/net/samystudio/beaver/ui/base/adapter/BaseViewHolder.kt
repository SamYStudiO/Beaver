@file:Suppress("unused")

package net.samystudio.beaver.ui.base.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

@Suppress("LeakingThis")
abstract class BaseViewHolder<D>(itemView: View) :
    RecyclerView.ViewHolder(itemView),
    View.OnAttachStateChangeListener {
    var data: D? = null
        set(value) {
            if (field != value) {
                field = value
                onDataChanged()
            }
        }

    abstract fun onDataChanged()
}
