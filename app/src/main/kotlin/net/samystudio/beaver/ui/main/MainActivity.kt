package net.samystudio.beaver.ui.main

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import net.samystudio.beaver.NavigationMainDirections
import net.samystudio.beaver.R
import net.samystudio.beaver.data.handleStates
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

        lifecycleScope.launchWhenResumed {
            viewModel.userConnectedState.collect {
                if (!it && findNavController(R.id.nav_host).currentDestination?.id != R.id.authenticatorFragment)
                    findNavController(R.id.nav_host).navigate(NavigationMainDirections.actionGlobalAuthenticatorFragment())
            }
            viewModel.googleApiAvailabilityState.collect { state ->
                state.handleStates(
                    failed = {
                        if (!(
                            it is MainActivityViewModel.GoogleApiAvailabilityException &&
                                it.isResolvable &&
                                it.googleApiAvailability.showErrorDialogFragment(
                                        this@MainActivity,
                                        it.status,
                                        0
                                    )
                            )
                        ) {
                            findNavController(R.id.nav_host).navigate(
                                NavigationMainDirections.actionGlobalAlertDialog(
                                    titleRes = R.string.global_error_title,
                                    messageRes = R.string.global_error_message,
                                    positiveButton = "retry",
                                    negativeButton = "quit",
                                    cancelable = false,
                                )
                            )
                        }
                    }
                )
            }
        }
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
