package net.samystudio.beaver.ui.main.authenticator

import android.accounts.AccountManager
import android.app.Activity
import android.arch.lifecycle.LiveData
import android.content.Intent
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.remote.CommandRequestState
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataPushViewModel
import net.samystudio.beaver.ui.common.viewmodel.RxCompletableCommand
import net.samystudio.beaver.ui.main.MainActivityViewModel
import javax.inject.Inject

@FragmentScope
class AuthenticatorFragmentViewModel
@Inject
constructor(activityViewModel: MainActivityViewModel) :
    BaseFragmentViewModel(activityViewModel), DataPushViewModel
{
    private val _dataPushCommand: RxCompletableCommand = RxCompletableCommand()
    override val dataPushCommand: LiveData<CommandRequestState> =
        _dataPushCommand.apply { disposables.add(this) }
    override val title: String?
        get() = null

    fun signIn(email: String, password: String)
    {
        _dataPushCommand.call(
            userManager
                .signIn(email, password)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess({ handleSignResult(email, password) })
                .toCompletable()
        )
    }

    fun signUp(email: String, password: String)
    {
        _dataPushCommand.call(
            userManager
                .signUp(email, password)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess({ handleSignResult(email, password) })
                .toCompletable()
        )
    }

    private fun handleSignResult(email: String, password: String)
    {
        val bundle = Bundle()
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, email)
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, BuildConfig.APPLICATION_ID)
        bundle.putString(AccountManager.KEY_PASSWORD, password)

        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
    }
}