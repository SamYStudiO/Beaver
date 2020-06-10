package net.samystudio.beaver.ui.base.viewmodel

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {
    @Inject
    protected lateinit var application: Application
    protected val disposables = CompositeDisposable()

    @CallSuper
    override fun onCleared() = disposables.dispose()
}
