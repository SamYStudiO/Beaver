package net.samystudio.beaver.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import dagger.hilt.android.AndroidEntryPoint
import net.samystudio.beaver.R
import net.samystudio.beaver.databinding.ActivityMainBinding
import net.samystudio.beaver.ui.base.activity.BaseToolbarActivity

@AndroidEntryPoint
class MainActivity : BaseToolbarActivity<ActivityMainBinding, MainActivityViewModel>() {
    override val navControllerId: Int = R.id.nav_host
    override val binding: ActivityMainBinding by viewBinding { ActivityMainBinding.inflate(it) }
    override val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        // We set launch screen theme from manifest, we need to get back to our Theme to remove
        // launch screen.
        setTheme(R.style.Theme_MyApp)

        super.onCreate(savedInstanceState)
    }

    override fun getToolbar(): Toolbar = binding.header

    override fun onSupportNavigateUp(): Boolean = navController.navigateUp()

}
