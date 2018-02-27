package net.samystudio.beaver.ui.launch

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_launch.*
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.fragment.BaseDataFragment
import net.samystudio.beaver.ui.main.MainActivity

class LaunchFragment : BaseDataFragment<LaunchViewModel>()
{
    override val viewModelClass: Class<LaunchViewModel> = LaunchViewModel::class.java
    override val layoutViewRes: Int = R.layout.fragment_launch

    override fun init(savedInstanceState: Bundle?)
    {
        super.init(savedInstanceState)

        button.setOnClickListener({
                                      startActivity(MainActivity::class.java,
                                                    null,
                                                    null,
                                                    null,
                                                    true)
                                  })
    }
}