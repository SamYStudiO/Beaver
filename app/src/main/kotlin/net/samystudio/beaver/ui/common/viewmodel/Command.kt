@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.samystudio.beaver.ui.common.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.support.annotation.MainThread
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

open class Command<T> : MutableLiveData<T>()
{
    private val pending: AtomicBoolean = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<T>)
    {
        if (hasActiveObservers())
            Timber.w("Multiple observers registered but only one will be notified of changes.")

        super.observe(owner,
                      Observer<T> { t ->
                          if (pending.compareAndSet(true, false))
                          {
                              observer.onChanged(t)
                          }
                      })
    }

    @MainThread
    override fun setValue(t: T?)
    {
        pending.set(true)
        super.setValue(t)
    }
}