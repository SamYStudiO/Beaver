@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")

package net.samystudio.beaver.ui.base.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.MenuItem
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager

abstract class BaseFragmentViewModel
constructor(fragmentNavigationManager: FragmentNavigationManager) :
    BaseViewControllerViewModel(fragmentNavigationManager)
{
    val titleObservable: MutableLiveData<String> = MutableLiveData()
    abstract val defaultTitle: String?

    @CallSuper
    override fun handleRestoreState(intent: Intent,
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

    fun setResult(code: Int, intent: Intent? = null)
    {
        val fragment = getCurrentFragment() as BaseFragment<*>?
        fragment?.targetFragment?.onActivityResult(fragment.targetRequestCode, code, intent)
    }

    companion object
    {
        const val TITLE_OBSERVABLE: String = "BaseFragmentViewModel:titleObservable"
    }
}