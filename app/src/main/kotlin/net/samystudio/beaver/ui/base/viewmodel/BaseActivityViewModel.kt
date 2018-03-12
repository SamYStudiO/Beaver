@file:Suppress("PropertyName", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
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

    /**
     * Convenient method to handle all [android.app.Activity] parameters at once.
     * This is called from view onResume, so may be called several time during view lifecycle. You
     * should make sure you've not already consume [intent] and [savedInstanceState] parameters
     * otherwise in some circumstance it could lead to unexpected behaviours (for example a
     * [android.widget.Toast] popping though it already has been consumed).
     *
     * @param intent [Intent] same as [android.app.Activity.getIntent].
     * @param savedInstanceState [Bundle] same as [android.app.Activity.onCreate].
     * @param resultRequestCode same as [android.app.Activity.onActivityResult].
     * @param resultCode same as [android.app.Activity.onActivityResult].
     * @param resultData same as [android.app.Activity.onActivityResult].
     */
    @CallSuper
    open fun handleState(intent: Intent,
                         savedInstanceState: Bundle?,
                         resultRequestCode: Int?,
                         resultCode: Int?,
                         resultData: Intent?)
    {
        _titleObservable.value = savedInstanceState?.getString(TITLE_OBSERVABLE, null)
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