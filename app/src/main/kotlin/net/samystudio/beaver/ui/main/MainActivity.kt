package net.samystudio.beaver.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import net.samystudio.beaver.NavigationMainDirections
import net.samystudio.beaver.R
import net.samystudio.beaver.databinding.ActivityMainBinding
import net.samystudio.beaver.ui.common.dialog.LaunchDialog
import net.samystudio.beaver.ui.common.dialog.LoaderDialog
import net.samystudio.beaver.util.toggleLightSystemBars
import net.samystudio.beaver.util.viewBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private val binding by viewBinding { ActivityMainBinding.inflate(it) }
    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        // We set launch screen theme from manifest, we need to get back to our Theme to remove
        // launch screen.
        setTheme(R.style.Theme_MyApp)
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        toggleLightSystemBars(true)

        /*
        We don't use navigation component here because when starting we'll navigate home screen and
        if a dialog is opened meanwhile it will be dismissed and we don't want this LaunchDialog to
        be dismissed while it has not finished initializing things up + we use
        LoaderDialog::class.simpleName to make sure no any other LoaderDialog is opened on top of
        launch screen dialog(since no LoaderDialog may be opened while another one is already opened
        when using LoaderDialog extensions Fragment.showLoaderDialog/Fragment.hideLoaderDialog).
        */
        LaunchDialog().also {
            if (supportFragmentManager.findFragmentByTag(LoaderDialog::class.simpleName) == null)
                it.show(supportFragmentManager, LoaderDialog::class.simpleName)
        }

        viewModel.userStatusLiveData.observe(
            this,
            {
                if (!it && findNavController(R.id.nav_host).currentDestination?.id != R.id.authenticatorFragment)
                    findNavController(R.id.nav_host).navigate(NavigationMainDirections.actionGlobalAuthenticatorFragment())
            }
        )
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        findNavController(R.id.nav_host).addOnDestinationChangedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.nav_host).navigateUp()

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
    }
}
