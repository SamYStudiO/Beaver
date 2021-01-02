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
import net.samystudio.beaver.data.ResultAsyncState
import net.samystudio.beaver.databinding.FragmentUserProfileBinding
import net.samystudio.beaver.ext.*

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
        toggleLightBars(true)
        hideLoaderDialog()
        postponeEnterTransition()

        binding.disconnect.setOnClickListener {
            viewModel.disconnect()
        }

        viewModel.userLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ResultAsyncState.Started -> {
                    showLoaderDialog()
                }
                is ResultAsyncState.Completed -> {
                    binding.textView.text = it.data.toString()
                    hideLoaderDialog()
                    startPostponedEnterTransition()
                }
                is ResultAsyncState.Failed -> {
                    hideLoaderDialog()
                    findNavController().popBackStack()
                    findNavController().navigate(UserProfileFragmentDirections.actionGlobalGenericErrorDialog())
                }
            }
        })
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
