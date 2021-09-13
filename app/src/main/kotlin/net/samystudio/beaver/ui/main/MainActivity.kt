package net.samystudio.beaver.ui.main

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import net.samystudio.beaver.NavigationMainDirections
import net.samystudio.beaver.R
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.manager.GoogleApiAvailabilityManager
import net.samystudio.beaver.databinding.ActivityMainBinding
import net.samystudio.beaver.ui.common.dialog.AlertDialog
import net.samystudio.beaver.util.toggleLightSystemBars
import net.samystudio.beaver.util.viewBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private val binding by viewBinding { ActivityMainBinding.inflate(it) }
    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        toggleLightSystemBars(true)

        binding.root.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if the initial data is ready.
                    return if (viewModel.isReady) {
                        binding.root.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )

        supportFragmentManager.findFragmentById(R.id.nav_host)?.childFragmentManager?.setFragmentResultListener(
            AlertDialog.REQUEST_KEY_CLICK_POSITIVE,
            this,
            { _, _ ->
                viewModel.retry()
            }
        )

        supportFragmentManager.findFragmentById(R.id.nav_host)?.childFragmentManager?.setFragmentResultListener(
            AlertDialog.REQUEST_KEY_CLICK_NEGATIVE,
            this,
            { _, _ ->
                finish()
            }
        )

        viewModel.initializationLiveData.observe(
            this,
            {
                when (it) {
                    is AsyncState.Failed -> {
                        if (!(
                            it.error is GoogleApiAvailabilityManager.GoogleApiAvailabilityException &&
                                it.error.isResolvable &&
                                it.error.googleApiAvailability.showErrorDialogFragment(
                                        this,
                                        it.error.status,
                                        0
                                    )
                            )
                        ) {
                            findNavController(R.id.nav_host).navigate(
                                NavigationMainDirections.actionGlobalAlertDialog(
                                    titleRes = R.string.error_title,
                                    messageRes = R.string.error_message,
                                    positiveButtonRes = R.string.retry,
                                    negativeButtonRes = R.string.quit,
                                    cancelable = false,
                                )
                            )
                        }
                    }
                    else -> Unit
                }
            }
        )

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
