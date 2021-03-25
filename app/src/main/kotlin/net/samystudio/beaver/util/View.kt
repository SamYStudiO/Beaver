@file:Suppress("unused")

package net.samystudio.beaver.util

import android.view.View
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal.
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are.
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

fun View.multipleClick(clickCount: Int = 2): Observable<Int> =
    clicks()
        .share()
        .let {
            it.buffer(it.debounce(if (clickCount > 3) 300 else 200, TimeUnit.MILLISECONDS))
        }
        .filter { it.size % clickCount == 0 }
        .map { clickCount }
        .observeOn(AndroidSchedulers.mainThread())
