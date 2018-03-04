package net.samystudio.beaver.ui.base.viewmodel

import android.accounts.AccountManager
import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import net.samystudio.beaver.data.local.SharedPreferencesManager

abstract class BaseFragmentViewModel
constructor(application: Application,
            accountManager: AccountManager,
            sharedPreferencesManager: SharedPreferencesManager,
            val activityViewModel: BaseActivityViewModel) :
    BaseViewControllerViewModel(application, accountManager, sharedPreferencesManager)
{
    val titleObservable: MutableLiveData<String> = MutableLiveData()
    abstract val defaultTitle: String?

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