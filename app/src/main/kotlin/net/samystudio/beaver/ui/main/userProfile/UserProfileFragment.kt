package net.samystudio.beaver.ui.main.userProfile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import net.samystudio.beaver.R
import net.samystudio.beaver.data.handleStatesFromFragmentWithLoaderDialog
import net.samystudio.beaver.databinding.FragmentUserProfileBinding
import net.samystudio.beaver.util.TRANSITION_DURATION
import net.samystudio.beaver.util.popBackStack
import net.samystudio.beaver.util.toggleLightSystemBars
import net.samystudio.beaver.util.viewBinding

@AndroidEntryPoint
class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {
    private val binding by viewBinding { FragmentUserProfileBinding.bind(it) }
    private val viewModel by viewModels<UserProfileFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enterTransition = MaterialFadeThrough().apply {
            duration = TRANSITION_DURATION
        }
        postponeEnterTransition()

        binding.textView.applyInsetter {
            type(statusBars = true) {
                padding(top = true)
            }
        }
        binding.disconnect.applyInsetter {
            type(navigationBars = true) {
                margin(bottom = true)
            }
        }

        binding.disconnect.setOnClickListener {
            viewModel.disconnect()
        }

        viewModel.userLiveData.observe(viewLifecycleOwner) { state ->
            state.handleStatesFromFragmentWithLoaderDialog(
                this,
                failed = {
                    popBackStack()
                }
            ) {
                binding.textView.text = it.toString()
                startPostponedEnterTransition()
            }
        }

        viewModel.logoutLiveData.observe(viewLifecycleOwner) {
            it.handleStatesFromFragmentWithLoaderDialog(this)
        }
    }

    override fun onResume() {
        super.onResume()
        toggleLightSystemBars(true)
    }
}
