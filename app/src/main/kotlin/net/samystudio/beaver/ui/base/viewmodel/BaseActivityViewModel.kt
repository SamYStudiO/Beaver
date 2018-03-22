@file:Suppress("PropertyName", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.support.annotation.CallSuper

abstract class BaseActivityViewModel : BaseViewControllerViewModel()
{
    protected val _titleObservable: MutableLiveData<String> = MutableLiveData()
    val titleObservable: LiveData<String> = _titleObservable
    var title
        get() = _titleObservable.value
        set(value)
        {
            _titleObservable.value = value
        }

    @CallSuper
    override fun handleRestoreInstanceState(savedViewState: Bundle)
    {
        _titleObservable.value = savedViewState.getString(TITLE_OBSERVABLE, null)
    }

    @CallSuper
    override fun handleSaveInstanceState(outState: Bundle)
    {
        super.handleSaveInstanceState(outState)

        outState.putString(TITLE_OBSERVABLE, _titleObservable.value)
    }

    companion object
    {
        private const val TITLE_OBSERVABLE: String = "BaseActivityViewModel:titleObservable"
    }
}