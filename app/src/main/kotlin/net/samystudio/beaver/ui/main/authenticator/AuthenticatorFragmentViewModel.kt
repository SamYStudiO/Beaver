package net.samystudio.beaver.ui.main.authenticator

import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.manager.AuthenticatorRepositoryManager
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ext.getClassTag
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataPushViewModel
import net.samystudio.beaver.ui.common.viewmodel.CompletableRequestLiveData
import javax.inject.Inject

@FragmentScope
class AuthenticatorFragmentViewModel @Inject constructor(private val authenticatorRepositoryManager: AuthenticatorRepositoryManager) :
    BaseFragmentViewModel(), DataPushViewModel {
    private val _dataPushCompletable: CompletableRequestLiveData = CompletableRequestLiveData()
    override val dataPushCompletable: LiveData<AsyncState> = _dataPushCompletable

    fun <T : AuthenticatorUserFlow> addUserFlow(observable: Observable<T>) {
        disposables.add(observable.flatMap { userFlow ->
            when (userFlow) {
                is AuthenticatorUserFlow.SignIn ->
                    _dataPushCompletable.bind(
                        authenticatorRepositoryManager.signIn(
                            userFlow.email,
                            userFlow.password
                        ).observeOn(AndroidSchedulers.mainThread())
                    ).doOnNext {
                        if (it is AsyncState.Completed)
                            handleSignResult(userFlow.email, userFlow.password)
                    }
                is AuthenticatorUserFlow.SignUp ->
                    _dataPushCompletable.bind(
                        authenticatorRepositoryManager.signUp(
                            userFlow.email,
                            userFlow.password
                        ).observeOn(AndroidSchedulers.mainThread())
                    ).doOnNext {
                        if (it is AsyncState.Completed)
                            handleSignResult(userFlow.email, userFlow.password)
                    }
                else ->
                    Observable.just(Observable.error<Unit> {
                        IllegalArgumentException("Unknown user flow ${userFlow.getClassTag()}.")
                    })
            }
        }.subscribe())
    }

    private fun handleSignResult(email: String, password: String) {
        val intent = Intent()
        intent.putExtras(
            bundleOf(
                AccountManager.KEY_ACCOUNT_NAME to email,
                AccountManager.KEY_ACCOUNT_TYPE to BuildConfig.APPLICATION_ID,
                AccountManager.KEY_PASSWORD to password
            )
        )
        setResult(Activity.RESULT_OK, intent)
    }
}