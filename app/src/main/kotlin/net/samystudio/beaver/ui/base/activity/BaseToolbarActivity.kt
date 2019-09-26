package net.samystudio.beaver.ui.base.activity

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.setupActionBarWithNavController
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel

abstract class BaseToolbarActivity<VM : BaseActivityViewModel> : BaseActivity<VM>() {
    protected abstract val toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)
    }
}