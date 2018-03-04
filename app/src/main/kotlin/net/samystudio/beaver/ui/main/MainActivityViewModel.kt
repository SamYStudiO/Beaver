package net.samystudio.beaver.ui.main

import android.accounts.AccountManager
import android.app.Application
import android.os.Bundle
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.local.SharedPreferencesManager
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ext.getCurrentAccount
import net.samystudio.beaver.ui.authenticator.AuthenticatorActivity
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import net.samystudio.beaver.ui.main.home.HomeFragment
import javax.inject.Inject

@ActivityScope
class MainActivityViewModel
@Inject
constructor(application: Application,
            fragmentNavigationManager: FragmentNavigationManager,
            private val accountManager: AccountManager,
            private val sharedPreferencesManager: SharedPreferencesManager) :
    BaseActivityViewModel(application, fragmentNavigationManager)
{
    override val defaultFragmentClass: Class<out BaseFragment<*>>
        get() = HomeFragment::class.java
    override val defaultFragmentBundle: Bundle?
        get() = null

    override fun handleReady()
    {
        super.handleReady()

        val account = accountManager.getCurrentAccount(BuildConfig.APPLICATION_ID,
                                                       sharedPreferencesManager.accountName)

        if (account != null)
            onBackStackChanged()
        else
            startActivity(AuthenticatorActivity::class.java)
    }
}