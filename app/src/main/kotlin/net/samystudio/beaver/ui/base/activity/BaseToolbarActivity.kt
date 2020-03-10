package net.samystudio.beaver.ui.base.activity

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.viewbinding.ViewBinding
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel

abstract class BaseToolbarActivity<VB : ViewBinding, VM : BaseActivityViewModel> :
    BaseViewBindingActivity<VB, VM>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(getToolbar())
        setupActionBarWithNavController(navController)
    }

    abstract fun getToolbar(): Toolbar
}