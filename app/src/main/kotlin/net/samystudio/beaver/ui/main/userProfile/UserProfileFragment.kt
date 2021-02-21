package net.samystudio.beaver.ui.main.userProfile

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import net.samystudio.beaver.R
import net.samystudio.beaver.data.handleStatesFromFragmentWithLoaderDialog
import net.samystudio.beaver.databinding.FragmentUserProfileBinding
import net.samystudio.beaver.util.TRANSITION_DURATION
import net.samystudio.beaver.util.hideLoaderDialog
import net.samystudio.beaver.util.toggleLightSystemBars
import net.samystudio.beaver.util.viewBinding

@AndroidEntryPoint
class UserProfileFragment : Fragment(R.layout.fragment_user_profile), OnApplyWindowInsetsListener {
    private val binding by viewBinding { FragmentUserProfileBinding.bind(it) }
    private val viewModel by viewModels<UserProfileFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enterTransition = MaterialFadeThrough().apply {
            duration = TRANSITION_DURATION
        }
        ViewCompat.setOnApplyWindowInsetsListener(view, this)
        toggleLightSystemBars(true)
        hideLoaderDialog()
        postponeEnterTransition()

        binding.disconnect.setOnClickListener {
            viewModel.disconnect()
        }

        viewModel.userLiveData.observe(
            viewLifecycleOwner,
            { state ->
                state.handleStatesFromFragmentWithLoaderDialog(
                    this,
                    failed = {
                        findNavController().popBackStack()
                        findNavController().navigate(UserProfileFragmentDirections.actionGlobalGenericErrorDialog())
                    },
                    complete = {
                        binding.textView.text = it.toString()
                        startPostponedEnterTransition()
                    },
                )
            }
        )
    }

    override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
        val stableSystemBarsInsets =
            insets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars())

        binding.textView.updatePadding(top = stableSystemBarsInsets.top)
        binding.disconnect.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            bottomMargin = stableSystemBarsInsets.bottom
        }
        return insets
    }
}
