package net.samystudio.beaver.ui.main.authenticator

import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.data.toAsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel
import net.samystudio.beaver.ui.common.viewmodel.TriggerDataLiveData
import net.samystudio.beaver.ui.common.viewmodel.toSingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class AuthenticatorFragmentViewModel @Inject constructor(
    private val userManager: UserManager
) : BaseDisposablesViewModel() {
    private val _signLiveData = TriggerDataLiveData<Pair<String, String>, AsyncState>() {
        userManager.signIn(it.first, it.second).toAsyncState()
    }
    val signLiveData: LiveData<AsyncState> = _signLiveData.toSingleLiveEvent()

    fun signIn(email: String, password: String) {
        _signLiveData.trigger(email to password)
    }

    fun signUp(email: String, password: String) {
        _signLiveData.trigger(email to password)
    }
}
