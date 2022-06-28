package net.samystudio.beaver.ui.main.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import net.samystudio.beaver.data.remote.HomeApiInterface
import net.samystudio.beaver.data.toResultAsyncState
import net.samystudio.beaver.util.TriggerResultAsyncStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    homeApiInterface: HomeApiInterface
) : ViewModel() {
    val homeState = TriggerResultAsyncStateFlow(true) {
        flow {
            emit(homeApiInterface.home())
        }.toResultAsyncState()
    }
}
