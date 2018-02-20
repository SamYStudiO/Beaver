@file:Suppress("unused")

package net.samystudio.beaver.ui.base.viewmodel

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel()
{
    private val disposables = CompositeDisposable()

    protected fun addDisposable(disposable: Disposable) = disposables.add(disposable)

    @CallSuper
    override fun onCleared() = disposables.dispose()
}
