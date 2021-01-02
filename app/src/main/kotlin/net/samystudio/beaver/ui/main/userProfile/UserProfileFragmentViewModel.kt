package net.samystudio.beaver.ui.main.userProfile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.rxjava3.core.BackpressureStrategy
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.data.toResultAsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel

class UserProfileFragmentViewModel @ViewModelInject constructor(
    private val userManager: UserManager
) : BaseDisposablesViewModel() {
    val userLiveData = LiveDataReactiveStreams.fromPublisher(
        userManager.getUser().toResultAsyncState().toFlowable(BackpressureStrategy.LATEST)
    )

    fun disconnect() {
        userManager.disconnect()
    }
}
