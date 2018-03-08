package net.samystudio.beaver.ui.main.home

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.data.remote.HomeApiInterface
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.main.MainActivityViewModel
import javax.inject.Inject

@FragmentScope
class HomeFragmentViewModel
@Inject
constructor(activityViewModel: MainActivityViewModel,
            private val homeApiManager: HomeApiInterface) :
    BaseFragmentViewModel(activityViewModel)
{
    override val title: String?
        get() = "home"

    val homeObservable: MutableLiveData<Home> = MutableLiveData()

    override fun handleReady()
    {
        super.handleReady()

        if (homeObservable.value == null)
            homeApiManager.home()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ home -> homeObservable.value = home },
                           { throwable -> throwable.printStackTrace() })
    }
}