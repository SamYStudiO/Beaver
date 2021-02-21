package net.samystudio.beaver.ui.common.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.view.*
import androidx.fragment.app.viewModels
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.Disposable
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.R
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.manager.GoogleApiAvailabilityManager
import net.samystudio.beaver.databinding.DialogLaunchBinding
import net.samystudio.beaver.util.toggleLightSystemBars
import net.samystudio.beaver.util.viewBinding
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Launch screen to initialized things.
 * Open as a dialog to make sure launch screen is on top of everything.
 */
@AndroidEntryPoint
class LaunchDialog : AppCompatDialogFragment(), OnApplyWindowInsetsListener {
    private val binding by viewBinding { DialogLaunchBinding.bind(it) }
    private val viewModel by viewModels<LaunchDialogViewModel>()
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_MyApp_Dialog_Launcher)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_launch, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(view, this)
        binding.launchVersion.text = BuildConfig.FULL_VERSION_NAME

        setDialogPositiveClickListener { _ ->
            viewModel.retry()
        }
        setDialogNegativeClickListener { _ ->
            activity?.finish()
        }
    }

    override fun onStart() {
        super.onStart()
        disposable?.dispose()
        disposable = Completable.timer(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.root.transitionToState(R.id.idle)
                binding.root.addTransitionListener(object : TransitionAdapter() {
                    override fun onTransitionCompleted(
                        motionLayout: MotionLayout,
                        currentId: Int,
                    ) {
                        super.onTransitionCompleted(motionLayout, currentId)
                        binding.root.removeTransitionListener(this)
                        viewModel.initializationObservable.observe(
                            viewLifecycleOwner,
                            {
                                Timber.i("initializationObservable: %s", it)
                                when (it) {
                                    is AsyncState.Started -> {
                                    }
                                    is AsyncState.Completed -> hide()
                                    is AsyncState.Failed -> {
                                        if (!(
                                            it.error is GoogleApiAvailabilityManager.GoogleApiAvailabilityException &&
                                                it.error.isResolvable &&
                                                it.error.googleApiAvailability.showErrorDialogFragment(
                                                        requireActivity(),
                                                        it.error.status,
                                                        0
                                                    )
                                            )
                                        ) {
                                            // Don't use navigation as this launch dialog is not
                                            // launched from navigation component and we won't get
                                            // dialog result if we are from different fragmentManagers.
                                            AlertDialog.newInstance(
                                                titleRes = R.string.global_error_title,
                                                messageRes = R.string.global_error_message,
                                                positiveButton = "retry",
                                                negativeButton = "quit",
                                                cancelable = false,
                                            ).show(
                                                parentFragmentManager,
                                                AlertDialog::class.simpleName
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }
                })
            }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.let { WindowCompat.setDecorFitsSystemWindows(it, false) }
        dialog?.window?.setBackgroundDrawable(
            ColorDrawable(MaterialColors.getColor(requireContext(), R.attr.colorPrimary, 0))
        )
        toggleLightSystemBars(false)
    }

    override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
        val stableSystemBarsInsets =
            insets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            binding.launchLogo.updatePadding(
                top = stableSystemBarsInsets.top,
                bottom = stableSystemBarsInsets.bottom
            )

        binding.launchVersion.updatePadding(bottom = stableSystemBarsInsets.bottom)
        return insets
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable?.dispose()
    }

    private fun hide() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        disposable?.dispose()
        disposable = Completable.timer(2000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.root.transitionToState(R.id.end)
                binding.root.addTransitionListener(object : TransitionAdapter() {
                    override fun onTransitionCompleted(
                        motionLayout: MotionLayout,
                        currentId: Int,
                    ) {
                        super.onTransitionCompleted(motionLayout, currentId)
                        view?.doOnPreDraw {
                            binding.root.removeTransitionListener(this)
                            dismissAllowingStateLoss()
                        }
                    }
                })
            }
    }
}
