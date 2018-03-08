package net.samystudio.beaver.ui.authenticator

import android.accounts.AccountManager
import android.app.Activity
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import javax.inject.Inject

@FragmentScope
class AuthenticatorFragmentViewModel
@Inject
constructor(activityViewModel: AuthenticatorActivityViewModel) :
    BaseFragmentViewModel(activityViewModel)
{
    override val title: String?
        get() = "account"

    fun signIn(email: String, password: String)
    {
        disposables.add(userManager.signIn(email, password)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ handleSignResult(email, password) },
                                       { throwable -> throwable.printStackTrace() }))
    }

    fun signUp(email: String, password: String)
    {
        disposables.add(userManager.signUp(email, password)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ handleSignResult(email, password) },
                                       { throwable -> throwable.printStackTrace() }))
    }

    private fun handleSignResult(email: String, password: String)
    {
        val bundle = Bundle()
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, email)
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE,
                         BuildConfig.APPLICATION_ID)
        bundle.putString(AccountManager.KEY_PASSWORD, password)

        (activityViewModel as AuthenticatorActivityViewModel)
            .setAuthenticatorResult(bundle)

        // just to close activity
        activityViewModel.setResult(Activity.RESULT_OK, null)
    }
}