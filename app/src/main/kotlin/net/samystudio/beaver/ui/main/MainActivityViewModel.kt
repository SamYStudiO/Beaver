package net.samystudio.beaver.ui.main

import androidx.lifecycle.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(userManager: UserManager) :
    BaseDisposablesViewModel() {
    val userStatusLiveData =
        userManager.statusObservable
            .toFlowable(BackpressureStrategy.LATEST)
            .toLiveData()
}
