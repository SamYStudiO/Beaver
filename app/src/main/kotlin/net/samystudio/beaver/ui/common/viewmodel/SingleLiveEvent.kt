@file:Suppress("NOTHING_TO_INLINE", "unused")

package net.samystudio.beaver.ui.common.viewmodel

import androidx.annotation.MainThread
import androidx.collection.ArraySet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

/**
 * A [LiveData] that emit a new value only once per observers.
 * https://proandroiddev.com/livedata-with-single-events-2395dea972a8
 */
open class SingleLiveEvent<T> : MediatorLiveData<T>() {
    private val observers = ArraySet<ObserverWrapper<in T>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observe(owner, wrapper)
    }

    @MainThread
    override fun observeForever(observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observeForever(wrapper)
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        if (observer is ObserverWrapper && observers.remove(observer)) {
            super.removeObserver(observer)
            return
        }
        val iterator = observers.iterator()
        while (iterator.hasNext()) {
            val wrapper = iterator.next()
            if (wrapper.observer == observer) {
                iterator.remove()
                super.removeObserver(wrapper)
                break
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        observers.forEach { it.newValue() }
        super.setValue(t)
    }

    @MainThread
    fun call() {
        value = null
    }

    private class ObserverWrapper<T>(val observer: Observer<in T>) : Observer<T> {
        private var pending = false

        override fun onChanged(t: T?) {
            if (pending) {
                pending = false
                observer.onChanged(t)
            }
        }

        fun newValue() {
            pending = true
        }
    }
}

/**
 * Convert any existing [LiveData] to [SingleLiveEvent].
 */
inline fun <T> LiveData<T>.toSingleLiveEvent() =
    SingleLiveEvent<T>().apply {
        addSource(this@toSingleLiveEvent) {
            value = it
        }
    }
