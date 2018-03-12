package net.samystudio.beaver.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.bumptech.glide.RequestManager
import net.samystudio.beaver.R
import net.samystudio.beaver.di.qualifier.ActivityContext
import net.samystudio.beaver.ui.base.activity.BaseActivity
import net.samystudio.beaver.ui.base.fragment.BaseSimpleFragment
import net.samystudio.beaver.ui.main.authenticator.AuthenticatorFragment
import net.samystudio.beaver.ui.main.home.HomeFragment
import javax.inject.Inject

class MainActivity : BaseActivity<MainActivityViewModel>()
{
    override val defaultFragmentClass: Class<out BaseSimpleFragment>
        get() = HomeFragment::class.java
    override val defaultFragmentBundle: Bundle?
        get() = null
    override val layoutViewRes: Int
        get() = R.layout.activity_main
    override val viewModelClass: Class<MainActivityViewModel>
        get() = MainActivityViewModel::class.java

    @Inject
    @field:ActivityContext
    protected lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?)
    {
        // We set launch screen theme from manifest, we need to get back to our Theme to remove
        // launch screen.
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
    }

    override fun onViewModelCreated(savedInstanceState: Bundle?)
    {
        super.onViewModelCreated(savedInstanceState)

        viewModel.requestAuthenticatorObservable.observe(this, Observer {

            // getGenericErrorDialog(this)
            AuthenticatorFragment.newInstance(MainActivityViewModel.AUTHENTICATOR_REQUEST_CODE)
                .showNow(supportFragmentManager, AuthenticatorFragment::class.java.name)
        })

        viewModel.userStatusObservable.observe(this, Observer {

            /*it?.let {
                if (it)
                    onBackStackChanged()
                else
                    AuthenticatorFragment().showNow(supportFragmentManager,
                                                    AuthenticatorFragment::class.java.name)
            }*/
        })

        onBackStackChanged()
    }
}
