@file:Suppress("unused")

package net.samystudio.beaver.ui.base.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.plusAssign

@Suppress("LeakingThis")
abstract class BaseViewHolder<D>(itemView: View) :
    RecyclerView.ViewHolder(itemView),
    View.OnAttachStateChangeListener {
    private var disposables: CompositeDisposable? = null
    var data: D? = null
        set(value) {
            if (field != value) {
                field = value
                onDataChanged()
            }
        }

    init {
        itemView.addOnAttachStateChangeListener(this)
    }

    override fun onViewAttachedToWindow(v: View) {
        disposables = CompositeDisposable()
    }

    override fun onViewDetachedFromWindow(v: View) {
        disposables?.dispose()
    }

    fun addDisposable(disposable: Disposable) {
        disposables?.let { it += disposable }
    }

    fun removeDisposable(disposable: Disposable) {
        disposables?.remove(disposable)
    }

    abstract fun onDataChanged()
}
