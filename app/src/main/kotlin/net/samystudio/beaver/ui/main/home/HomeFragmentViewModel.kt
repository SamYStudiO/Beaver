package net.samystudio.beaver.ui.main.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import net.samystudio.beaver.data.remote.HomeApiInterface
import net.samystudio.beaver.ui.common.viewmodel.TriggerOutStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    homeApiInterface: HomeApiInterface
) : ViewModel() {
    val homeState = TriggerOutStateFlow(true) {
        homeApiInterface.home()
    }
}
