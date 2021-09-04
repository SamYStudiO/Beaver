package net.samystudio.beaver.ui.main.authenticator

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.processors.PublishProcessor
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.data.toAsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticatorFragmentViewModel @Inject constructor(
    private val userManager: UserManager
) : BaseDisposablesViewModel() {
    private val _signProcessor = PublishProcessor.create<Pair<String, String>>()
    val signLiveData: LiveData<AsyncState> = _signProcessor.flatMap {
        userManager.signIn(it.first, it.second).toAsyncState()
    }.toLiveData()

    fun signIn(email: String, password: String) {
        _signProcessor.onNext(email to password)
    }

    fun signUp(email: String, password: String) {
        _signProcessor.onNext(email to password)
    }
}
