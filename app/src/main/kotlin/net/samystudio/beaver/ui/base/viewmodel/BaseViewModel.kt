@file:Suppress("MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseViewModel : ViewModel()
{
    @SuppressLint("StaticFieldLeak")
    @Inject
    protected lateinit var application: Application

    protected val disposables = CompositeDisposable()

    @CallSuper
    override fun onCleared() = disposables.dispose()
}
