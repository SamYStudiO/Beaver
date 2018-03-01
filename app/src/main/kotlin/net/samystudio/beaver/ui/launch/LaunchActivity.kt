package net.samystudio.beaver.ui.launch

import android.os.Bundle
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.activity.BaseActivity

class LaunchActivity : BaseActivity<LaunchActivityViewModel>()
{
    override val layoutViewRes: Int
        get() = R.layout.activity_launch
    override val viewModelClass: Class<LaunchActivityViewModel>
        get() = LaunchActivityViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
