package net.samystudio.beaver.ui.main.authenticator

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Intent
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.manager.AuthenticatorRepositoryManager
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ext.getClassTag
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataPushViewModel
import net.samystudio.beaver.ui.common.navigation.NavigationRequest
import net.samystudio.beaver.ui.common.viewmodel.AsyncStateLiveData
import javax.inject.Inject

@FragmentScope
class AuthenticatorFragmentViewModel @Inject constructor(private val authenticatorRepositoryManager: AuthenticatorRepositoryManager) :
    BaseFragmentViewModel(), DataPushViewModel {
    private val _dataPushCompletable: AsyncStateLiveData = AsyncStateLiveData()
    override val dataPushCompletable: LiveData<AsyncState> = _dataPushCompletable
    private val _signInVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val _signUpVisibility: MutableLiveData<Boolean> = MutableLiveData()
    val signInVisibility: LiveData<Boolean> = _signInVisibility
    val signUpVisibility: LiveData<Boolean> = _signUpVisibility
    private var authenticatorResponse: AccountAuthenticatorResponse? = null
    private lateinit var intent: Intent

    override fun handleIntent(intent: Intent) {
        super.handleIntent(intent)

        this.intent = intent

        authenticatorResponse =
                intent.getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE)
        authenticatorResponse?.onRequestContinued()

        _signInVisibility.value = !intent.hasExtra(UserManager.KEY_CREATE_ACCOUNT)
        _signUpVisibility.value = !intent.hasExtra(UserManager.KEY_CONFIRM_ACCOUNT)
    }

    fun <T : AuthenticatorUserFlow> addUserFlow(observable: Observable<T>) {
        disposables.add(observable.flatMap { userFlow ->
            when (userFlow) {
                is AuthenticatorUserFlow.SignIn ->
                    _dataPushCompletable.bind(
                        authenticatorRepositoryManager.signIn(userFlow.email, userFlow.password)
                    ).observeOn(AndroidSchedulers.mainThread())
                        .doOnNext {
                            if (it is AsyncState.Completed) handleSignResult(
                                userFlow.email,
                                userFlow.password
                            )
                        }
                is AuthenticatorUserFlow.SignUp ->
                    _dataPushCompletable.bind(
                        authenticatorRepositoryManager.signUp(userFlow.email, userFlow.password)
                    ).observeOn(AndroidSchedulers.mainThread())
                        .doOnNext {
                            if (it is AsyncState.Completed) handleSignResult(
                                userFlow.email,
                                userFlow.password
                            )
                        }
                else -> Observable.error<AsyncState> {
                    IllegalArgumentException("Unknown user flow ${userFlow.getClassTag()}.")
                }
            }
        }.subscribe())
    }

    private fun handleSignResult(email: String, password: String) {
        authenticatorResponse?.onResult(
            bundleOf(
                AccountManager.KEY_ACCOUNT_NAME to email,
                AccountManager.KEY_ACCOUNT_TYPE to UserManager.ACCOUNT_TYPE,
                AccountManager.KEY_PASSWORD to password
            )
        )

        authenticatorResponse = null
        navigate(NavigationRequest.Pop())
    }

    override fun onCleared() {
        super.onCleared()

        authenticatorResponse?.onError(
            AccountManager.ERROR_CODE_CANCELED,
            "Authentication was cancelled"
        )
        authenticatorResponse = null

        intent.removeExtra(UserManager.KEY_CREATE_ACCOUNT)
        intent.removeExtra(UserManager.KEY_CONFIRM_ACCOUNT)
    }
}