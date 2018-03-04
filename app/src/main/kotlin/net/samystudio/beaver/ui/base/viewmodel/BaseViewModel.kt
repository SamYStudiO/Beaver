@file:Suppress("MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(application: Application) : AndroidViewModel(application)
{
    protected val disposables = CompositeDisposable()

    @CallSuper
    override fun onCleared() = disposables.dispose()
}
