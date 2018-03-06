package net.samystudio.beaver.ui.base.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper

abstract class BaseFragmentViewModel
constructor(protected val activityViewModel: BaseActivityViewModel) : BaseViewControllerViewModel()
{
    protected abstract val defaultTitle: String?
    val titleObservable: MutableLiveData<String> = MutableLiveData()

    @CallSuper
    override fun handleState(intent: Intent,
                             savedInstanceState: Bundle?,
                             arguments: Bundle?)
    {
        titleObservable.value =
                savedInstanceState?.getString(TITLE_OBSERVABLE) ?: defaultTitle
    }

    @CallSuper
    override fun handleSaveInstanceState(outState: Bundle)
    {
        super.handleSaveInstanceState(outState)

        outState.putString(TITLE_OBSERVABLE, titleObservable.value)
    }

    companion object
    {
        private const val TITLE_OBSERVABLE: String = "BaseFragmentViewModel:titleObservable"
    }
}