@file:Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")

package net.samystudio.beaver.ui.base.viewmodel

import android.accounts.AccountManager
import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.local.SharedPreferencesManager
import net.samystudio.beaver.ext.getCurrentAccount

abstract class BaseViewControllerViewModel
constructor(application: Application,
            val accountManager: AccountManager,
            val sharedPreferencesManager: SharedPreferencesManager) : BaseViewModel(application)
{
    val resultObservable: MutableLiveData<Result> = MutableLiveData()
    val accountStatusObservable: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * This is called from view onResume, so may be called several time during view lifecycle. You should
     * make sure you've not already consume these parameters, in some circumstance it could lead to
     * unexpected behaviours.
     * arguments is always null with activity view models.
     */
    open fun handleState(intent: Intent,
                         savedInstanceState: Bundle?,
                         arguments: Bundle? = null)
    {
    }

    @CallSuper
    open fun handleReady()
    {
        val account = accountManager.getCurrentAccount(BuildConfig.APPLICATION_ID,
                                                       sharedPreferencesManager.accountName)

        accountStatusObservable.value = account != null
    }

    open fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
    }

    open fun handleSaveInstanceState(outState: Bundle)
    {
    }

    /**
     * @see android.app.Activity.setResult
     */
    fun setResult(code: Int, intent: Intent?, finish: Boolean = true)
    {
        resultObservable.value = Result(code, intent, finish)
    }

    data class Result(var code: Int, var intent: Intent?, var finish: Boolean)
}