package net.samystudio.beaver.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.bumptech.glide.RequestManager
import net.samystudio.beaver.R
import net.samystudio.beaver.di.qualifier.ActivityContext
import net.samystudio.beaver.ui.authenticator.AuthenticatorActivity
import net.samystudio.beaver.ui.base.activity.BaseActivity
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.main.home.HomeFragment
import javax.inject.Inject

class MainActivity : BaseActivity<MainActivityViewModel>()
{
    override val defaultFragmentClass: Class<out BaseFragment>
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

        viewModel.userStatusObservable.observe(this, Observer { connected ->

            connected?.let {
                if (connected)
                    onBackStackChanged()
                else
                    fragmentNavigationManager.startActivity(AuthenticatorActivity::class.java)
            }
        })
    }
}
