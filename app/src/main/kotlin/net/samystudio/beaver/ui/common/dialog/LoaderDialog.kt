package net.samystudio.beaver.ui.common.dialog

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatDialogFragment
import net.samystudio.beaver.R
import net.samystudio.beaver.databinding.DialogLoaderBinding
import net.samystudio.beaver.util.TRANSITION_DURATION
import net.samystudio.beaver.util.viewBinding

class LoaderDialog : AppCompatDialogFragment() {
    private val binding by viewBinding { DialogLoaderBinding.bind(it) }
    private var dismissRequested = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_MyApp_Dialog_Loader)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_loader, container, false)

    override fun dismiss() {
        dismissAllowingStateLoss()
    }

    override fun dismissAllowingStateLoss() {
        // Don't dismiss while we're animating, just wait to make loader animation smoother.
        if (view == null || (requireView().alpha > 0f && requireView().alpha < 1f))
            dismissRequested = true
        else hide()
    }

    override fun onStart() {
        super.onStart()

        if (dismissRequested) super.dismissAllowingStateLoss()
        else {
            binding.logo.alpha = 0f
            binding.progressBar.alpha = 0f
            binding.logo.translationY = -50 * resources.displayMetrics.density
            binding.logo.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(TRANSITION_DURATION).interpolator = OvershootInterpolator()
            binding.progressBar.animate()
                .alpha(1f)
                .setStartDelay(TRANSITION_DURATION)
                .setDuration(TRANSITION_DURATION)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        if (dismissRequested) hide()
                    }
                })
        }
    }

    private fun hide() {
        binding.progressBar.hide()
        binding.logo.animate()
            .translationY(50 * resources.displayMetrics.density)
            .alpha(0f)
            .setDuration(TRANSITION_DURATION).setInterpolator(AccelerateInterpolator(2f))
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super@LoaderDialog.dismissAllowingStateLoss()
                }
            })
    }
}
