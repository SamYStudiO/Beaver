@file:Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER", "PropertyName")

package net.samystudio.beaver.ui.base.viewmodel

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.OnAccountsUpdateListener
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.local.SharedPreferencesManager
import net.samystudio.beaver.ext.getCurrentAccount
import net.samystudio.beaver.ui.authenticator.AuthenticatorActivity
import javax.inject.Inject

abstract class BaseViewControllerViewModel : BaseViewModel(), OnAccountsUpdateListener
{
    @Inject
    protected lateinit var accountManager: AccountManager
    @Inject
    protected lateinit var sharedPreferencesManager: SharedPreferencesManager
    protected val _resultObservable: MutableLiveData<Result> = MutableLiveData()
    val resultObservable: LiveData<Result> = _resultObservable
    protected val _accountStatusObservable: MutableLiveData<Boolean> = MutableLiveData()
    val accountStatusObservable: LiveData<Boolean> = _accountStatusObservable

    @CallSuper
    open fun handleCreate()
    {
        accountManager.addOnAccountsUpdatedListener(this, null, true)
    }

    open fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
    }

    /**
     * This is called from view onResume, so may be called several time during view lifecycle. You
     * should make sure you've not already consume these parameters otherwise in some circumstance
     * it could lead to unexpected behaviours (for example a [android.widget.Toast] popping though
     * it already has been consumed).
     *
     * @param intent [Intent] from activity container, same as [android.app.Activity.getIntent].
     * @param savedInstanceState [Bundle] from activity or fragment container.
     * @param arguments [Bundle] from fragment container, same as
     * [android.support.v4.app.Fragment.getArguments], always null if container is an activity.
     * [requestCode] is always null with activity view models.
     * @param requestCode same as onActivityResult
     * @param resultCode same as onActivityResult
     * @param data same as onActivityResult
     */
    open fun handleState(intent: Intent,
                         savedInstanceState: Bundle?,
                         arguments: Bundle?,
                         requestCode: Int?,
                         resultCode: Int?,
                         data: Intent?)
    {
    }

    /**
     * View model is up and ready, all kind of params (intent, savedInstanceState, arguments) should
     * be handle now. Called right after [handleState].
     */
    @CallSuper
    open fun handleReady()
    {
    }

    open fun handleSaveInstanceState(outState: Bundle)
    {
    }

    override fun onAccountsUpdated(accounts: Array<out Account>?)
    {
        val account = accountManager.getCurrentAccount(BuildConfig.APPLICATION_ID,
                                                       sharedPreferencesManager.accountName)


        _accountStatusObservable.value = account != null &&
                accountManager.peekAuthToken(account,
                                             AuthenticatorActivity.DEFAULT_AUTH_TOKEN_TYPE) != null
    }

    /**
     * @see android.app.Activity.setResult
     */
    fun setResult(code: Int, intent: Intent?, finish: Boolean = true)
    {
        _resultObservable.value = Result(code, intent, finish)
    }

    override fun onCleared()
    {
        super.onCleared()
        accountManager.removeOnAccountsUpdatedListener(this)
    }

    data class Result(var code: Int, var intent: Intent?, var finish: Boolean)
}