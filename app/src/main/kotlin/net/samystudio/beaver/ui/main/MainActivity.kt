package net.samystudio.beaver.ui.main

import android.os.Bundle
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.activity.BaseActivity

class MainActivity : BaseActivity<MainActivityViewModel>()
{
    override val layoutViewRes: Int
        get() = R.layout.activity_main
    override val viewModelClass: Class<MainActivityViewModel>
        get() = MainActivityViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
    }
}
