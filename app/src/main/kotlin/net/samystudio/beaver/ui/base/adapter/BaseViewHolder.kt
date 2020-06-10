package net.samystudio.beaver.ui.base.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.plusAssign
import kotlinx.android.extensions.LayoutContainer

abstract class BaseViewHolder<D>(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer,
    View.OnAttachStateChangeListener {
    private var needDataChangedDispatch = false
    private var disposables: CompositeDisposable? = null
    var data: D? = null
        set(value) {
            if (field != value) {
                field = value
                if (itemView.isAttachedToWindow) onDataChanged()
                else needDataChangedDispatch = true
            }
        }

    init {
        @Suppress("LeakingThis")
        itemView.addOnAttachStateChangeListener(this)
    }

    override fun onViewAttachedToWindow(v: View?) {
        disposables = CompositeDisposable()

        if (needDataChangedDispatch) onDataChanged()
    }

    override fun onViewDetachedFromWindow(v: View?) {
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