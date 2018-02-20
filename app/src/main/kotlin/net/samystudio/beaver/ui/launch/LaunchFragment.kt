package net.samystudio.beaver.ui.launch

import android.os.Bundle
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import timber.log.Timber

class LaunchFragment : BaseFragment()
{
    override fun init(savedInstanceState: Bundle?)
    {
        Timber.d("init: %s", fragmentNavigationManager)
    }

    override fun getLayoutViewRes(): Int = 0

    override fun getDefaultTitle(): String = ""
}