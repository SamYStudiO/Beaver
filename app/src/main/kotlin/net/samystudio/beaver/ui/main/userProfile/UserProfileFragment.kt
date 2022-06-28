package net.samystudio.beaver.ui.main.userProfile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.launch
import net.samystudio.beaver.R
import net.samystudio.beaver.data.handleStatesFromFragmentWithLoaderDialog
import net.samystudio.beaver.databinding.FragmentUserProfileBinding
import net.samystudio.beaver.util.TRANSITION_DURATION
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.userState.collect { state ->
                        state.handleStatesFromFragmentWithLoaderDialog(
                            this@UserProfileFragment,
                            failed = {
                                findNavController().popBackStack()
                            },
                            complete = {
                                binding.textView.text = it.toString()
                                startPostponedEnterTransition()
                            },
                        )
                    }
                }
                launch {
                    viewModel.logoutState.collect {
                        it.handleStatesFromFragmentWithLoaderDialog(this@UserProfileFragment)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.userState.collect { state ->
                state.handleStatesFromFragmentWithLoaderDialog(
                    this@UserProfileFragment,
                    failed = {
                        findNavController().popBackStack()
                    },
                    complete = {
                        binding.textView.text = it.toString()
                        startPostponedEnterTransition()
                    },
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        toggleLightSystemBars(true)
    }
}
