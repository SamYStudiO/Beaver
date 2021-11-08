package net.samystudio.beaver.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import net.samystudio.beaver.ui.common.dialog.ErrorSource
import net.samystudio.beaver.ui.common.dialog.setDialogNegativeClickListener
import net.samystudio.beaver.ui.common.dialog.setDialogPositiveClickListener
import net.samystudio.beaver.util.toggleLightSystemBars
import net.samystudio.beaver.util.viewBinding
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private val binding by viewBinding { ActivityMainBinding.inflate(it) }
    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        splashScreen.setKeepVisibleCondition { !viewModel.isReady }

        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        toggleLightSystemBars(true)

        setDialogPositiveClickListener(R.id.nav_host, ERROR_REQUEST_CODE) {
            viewModel.retry()
        }

        setDialogNegativeClickListener(R.id.nav_host, ERROR_REQUEST_CODE) {
            finishAndRemoveTask()
        }

        viewModel.initializationLiveData.observe(
            this,
            {
                when (it) {
                    is AsyncState.Failed -> {
                        val resolvable =
                            it.error is GoogleApiAvailabilityManager.GoogleApiAvailabilityException &&
                                it.error.isResolvable &&
                                it.error.googleApiAvailability.showErrorDialogFragment(
                                    this,
                                    it.error.status,
                                    0
                                )
                        if (!resolvable) {
                            findNavController(R.id.nav_host).navigate(
                                NavigationMainDirections.actionGlobalErrorDialog(
                                    source = ErrorSource.APP,
                                    titleRes = R.string.error_title,
                                    message = it.error.message,
                                    positiveButtonRes = R.string.retry,
                                    negativeButtonRes = R.string.quit,
                                    cancelable = false,
                                    requestCode = ERROR_REQUEST_CODE
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

    companion object {
        private val ERROR_REQUEST_CODE = Random().nextInt()
    }
}
