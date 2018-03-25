package net.samystudio.beaver.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.activity.BaseActivity
import net.samystudio.beaver.ui.main.authenticator.AuthenticatorController

class MainActivity : BaseActivity<MainActivityViewModel>()
{
    override val layoutViewRes: Int = R.layout.activity_main
    override val viewModelClass: Class<MainActivityViewModel> = MainActivityViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?)
    {
        // We set launch screen theme from manifest, we need to get back to our Theme to remove
        // launch screen.
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
    }

    override fun onViewModelCreated()
    {
        super.onViewModelCreated()

        viewModel.userStatusObservable.observe(this, Observer {

            it?.let {
                if (it)
                    router.popCurrentController()
                else
                    AuthenticatorController().show(router)
            }
        })
    }
}
