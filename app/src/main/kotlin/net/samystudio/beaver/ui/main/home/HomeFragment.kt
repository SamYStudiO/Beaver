package net.samystudio.beaver.ui.main.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import net.samystudio.beaver.R
import net.samystudio.beaver.data.handleStatesFromFragmentWithLoaderDialog
import net.samystudio.beaver.databinding.FragmentHomeBinding
import net.samystudio.beaver.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), OnApplyWindowInsetsListener {
    private val binding by viewBinding { FragmentHomeBinding.bind(it) }
    private val viewModel by viewModels<HomeFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exitTransition = MaterialFadeThrough().apply {
            duration = TRANSITION_DURATION
        }
        ViewCompat.setOnApplyWindowInsetsListener(view, this)
        toggleLightStatusBars(false)
        toggleLightNavigationBars(true)
        hideLoaderDialog()

        binding.toolbar.title = "Home"
        binding.profileButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_home_to_userProfile))

        viewModel.homeLiveData.observe(
            viewLifecycleOwner,
            { state ->
                state.handleStatesFromFragmentWithLoaderDialog(
                    this,
                    failed = { findNavController().navigate(HomeFragmentDirections.actionGlobalGenericErrorDialog()) },
                    complete = { binding.textView.text = it.content },
                )
            }
        )
    }

    override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
        val stableSystemBarsInsets =
            insets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars())

        binding.toolbar.updatePadding(top = stableSystemBarsInsets.top)
        binding.profileButton.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            bottomMargin =
                stableSystemBarsInsets.bottom + resources.getDimensionPixelSize(R.dimen.screen_padding)
        }
        return insets
    }
}
