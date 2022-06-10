package net.samystudio.beaver.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.doOnPreDraw
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import dagger.hilt.android.AndroidEntryPoint
import net.samystudio.beaver.NavigationMainDirections
import net.samystudio.beaver.R
import net.samystudio.beaver.data.handleStates
import net.samystudio.beaver.data.repository.TokenException
import net.samystudio.beaver.databinding.ActivityMainBinding
import net.samystudio.beaver.ui.common.dialog.AlertDialog
import net.samystudio.beaver.ui.common.dialog.ErrorDialog
import net.samystudio.beaver.ui.common.dialog.setDialogNegativeClickListener
import net.samystudio.beaver.ui.common.dialog.setDialogPositiveClickListener
import net.samystudio.beaver.util.navigate
import net.samystudio.beaver.util.showErrorDialog
import net.samystudio.beaver.util.viewBinding
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private val binding by viewBinding { ActivityMainBinding.inflate(it) }
    private val viewModel by viewModels<MainActivityViewModel>()
    private val resolveApiAvailability =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK)
                viewModel.refreshData()
            else
                showErrorDialog(
                    R.id.nav_host,
                    positiveButtonRes = R.string.retry,
                    negativeButtonRes = R.string.quit,
                    cancelable = false,
                    requestCode = ERROR_REQUEST_CODE
                )
        }
    private var isReady: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { !isReady }

        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setDialogPositiveClickListener(R.id.nav_host, ERROR_REQUEST_CODE) {
            binding.root.doOnPreDraw {
                viewModel.refreshData()
            }
        }

        setDialogNegativeClickListener(R.id.nav_host, ERROR_REQUEST_CODE) {
            finishAndRemoveTask()
        }

        viewModel.appDataLiveData.observe(this) { state ->
            state.handleStates(
                failed = { throwable ->
                    isReady = true

                    when {
                        throwable is GoogleApiAvailabilityException && throwable.isResolvable -> {
                            throwable.googleApiAvailability.getErrorResolutionIntent(
                                this,
                                0,
                                ""
                            )?.let {
                                resolveApiAvailability.launch(it)
                            } ?: showInitializationErrorDialog(throwable)
                        }
                        throwable is TokenException ->
                            Unit // Ignore since logout is handled from token repository.
                        else ->
                            showInitializationErrorDialog(throwable)
                    }
                }
            ) {
                isReady = true
            }
        }

        viewModel.userStatusLiveData.observe(this) { connected ->
            if (!connected && findNavController(R.id.nav_host).currentDestination?.id != R.id.authenticatorFragment)
                navigate(
                    R.id.nav_host,
                    NavigationMainDirections.actionGlobalAuthenticatorFragment()
                )
            else {
                intent?.let { handleIntent(it) }
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        findNavController(R.id.nav_host).addOnDestinationChangedListener(this)
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshData()
    }

    override fun onDestroy() {
        findNavController(R.id.nav_host).removeOnDestinationChangedListener(this)
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (intent != null && viewModel.userIsConnected)
            handleIntent(intent)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        run {
            (destination as? FragmentNavigator.Destination)?.className
                ?: (destination as? DialogFragmentNavigator.Destination)?.className
        }?.let {
            viewModel.logScreen(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.nav_host).navigateUp()

    private fun showInitializationErrorDialog(throwable: Throwable) {
        // Don't use navigation component to make sure this dialog is always on top and is not
        // dismiss by navigating with navigation component.
        ErrorDialog().apply {
            arguments = bundleOf(
                ErrorDialog.KEY_ERROR_EXCEPTION to throwable,
                AlertDialog.KEY_POSITIVE_BUTTON_RES to R.string.retry,
                AlertDialog.KEY_NEGATIVE_BUTTON_RES to R.string.quit,
                AlertDialog.KEY_CANCELABLE to false,
                AlertDialog.KEY_REQUEST_CODE to ERROR_REQUEST_CODE,
            )
        }.show(supportFragmentManager, "")
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            else -> Unit // TODO handle it here
        }
        setIntent(null)
    }

    companion object {
        private val ERROR_REQUEST_CODE = Random().nextInt()
    }
}
