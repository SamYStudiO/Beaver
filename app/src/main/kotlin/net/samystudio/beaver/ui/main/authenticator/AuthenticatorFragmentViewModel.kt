package net.samystudio.beaver.ui.main.authenticator

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.data.toAsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel

class AuthenticatorFragmentViewModel @ViewModelInject constructor(
    private val userManager: UserManager
) : BaseDisposablesViewModel() {
    private val _signLiveData = MutableLiveData<AsyncState>()
    val signLiveData: LiveData<AsyncState> = _signLiveData

    fun signIn(email: String, password: String) {
        userManager.signIn(email, password)
            .toAsyncState()
            .subscribe { _signLiveData.postValue(it) }
            .addTo(disposables)
    }

    fun signUp(email: String, password: String) {
        userManager.signUp(email, password)
            .toAsyncState()
            .subscribe { _signLiveData.postValue(it) }
            .addTo(disposables)
    }
}
