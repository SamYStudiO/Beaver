package net.samystudio.beaver.ui.main.userProfile

import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.addTo
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.repository.UserRepository
import net.samystudio.beaver.data.toAsyncState
import net.samystudio.beaver.data.toResultAsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel
import net.samystudio.beaver.ui.common.viewmodel.toSingleLiveEvent
import net.samystudio.beaver.ui.common.viewmodel.toTriggerLiveData
import javax.inject.Inject

@HiltViewModel
class UserProfileFragmentViewModel @Inject constructor(
    userRepository: UserRepository
) : BaseDisposablesViewModel() {
    val userLiveData =
        userRepository.dataObservable
            .toResultAsyncState()
            .toTriggerLiveData()
    private val _logoutLiveData = userRepository.logout().toAsyncState().toTriggerLiveData().apply {
        addTo(disposables)
    }
    val logoutLiveData: LiveData<AsyncState> = _logoutLiveData.toSingleLiveEvent()

    fun disconnect() {
        _logoutLiveData.trigger()
    }
}
