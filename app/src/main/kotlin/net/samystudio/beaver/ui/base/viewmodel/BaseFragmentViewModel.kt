package net.samystudio.beaver.ui.base.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.MenuItem
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.base.fragment.dialog.BaseDialog
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager

abstract class BaseFragmentViewModel
constructor(application: Application,
            fragmentNavigationManager: FragmentNavigationManager,
            val activityViewModel: BaseActivityViewModel) :
    BaseViewControllerViewModel(application, fragmentNavigationManager)
{
    val titleObservable: MutableLiveData<String> = MutableLiveData()
    abstract val defaultTitle: String?

    @CallSuper
    override fun handleState(intent: Intent,
                             savedInstanceState: Bundle?,
                             arguments: Bundle?)
    {
        titleObservable.value =
                savedInstanceState?.getString(TITLE_OBSERVABLE) ?: defaultTitle
    }

    @CallSuper
    override fun handleSaveInstanceState(outState: Bundle)
    {
        super.handleSaveInstanceState(outState)

        outState.putString(TITLE_OBSERVABLE, titleObservable.value)
    }

    open fun willConsumeOptionsItem(item: MenuItem): Boolean
    {
        return false
    }

    final override fun setResult(code: Int, intent: Intent?, finish: Boolean)
    {
        val currentFragment: BaseFragment<*>? = getCurrentFragment()
        currentFragment?.targetFragment?.onActivityResult(currentFragment.targetRequestCode,
                                                          code,
                                                          intent)

        if (finish)
            (currentFragment as? BaseDialog)?.dismiss() ?: popBackStack()
    }

    companion object
    {
        private const val TITLE_OBSERVABLE: String = "BaseFragmentViewModel:titleObservable"
    }
}