@file:Suppress("ProtectedInFinal")

package net.samystudio.beaver.ui.main.authenticator

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import net.samystudio.beaver.R
import net.samystudio.beaver.data.handleStatesFromFragmentWithLoaderDialog
import net.samystudio.beaver.databinding.FragmentAuthenticatorBinding
import net.samystudio.beaver.util.*
import reactivecircus.flowbinding.android.widget.textChanges

@FlowPreview
@AndroidEntryPoint
class AuthenticatorFragment : Fragment(R.layout.fragment_authenticator) {
    private val binding by viewBinding { FragmentAuthenticatorBinding.bind(it) }
    private val viewModel by viewModels<AuthenticatorFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enterTransition =
            MaterialSharedAxis(MaterialSharedAxis.Y, true).apply { duration = TRANSITION_DURATION }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            }
        )

        binding.signInEmailLayout.applyInsetter {
            type(statusBars = true) {
                margin(top = true)
            }
        }

        binding.signIn.setOnClickListener {
            viewModel.signIn(
                binding.signInEmail.text.toString(),
                binding.signInPassword.text.toString()
            )
        }
        binding.signUp.setOnClickListener {
            viewModel.signUp(
                binding.signUpEmail.text.toString(),
                binding.signUpPassword.text.toString()
            )
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.accountName.collect {
                binding.signInEmail.setText(it)
            }

            viewModel.signInState.collect {
                it.handleStatesFromFragmentWithLoaderDialog(
                    this@AuthenticatorFragment,
                    failed = { findNavController().navigate(AuthenticatorFragmentDirections.actionGlobalGenericErrorDialog()) },
                    complete = { findNavController().popBackStack() },
                )
            }

            viewModel.signUpState.collect {
                it.handleStatesFromFragmentWithLoaderDialog(
                    this@AuthenticatorFragment,
                    failed = { findNavController().navigate(AuthenticatorFragmentDirections.actionGlobalGenericErrorDialog()) },
                    complete = { findNavController().popBackStack() },
                )
            }
        }

        combine(
            binding.signInEmail.textChanges()
                .debounce(500)
                .map {
                    val emailValid = it.validate(EMAIL_VALIDATOR)
                    binding.signInEmailLayout.error =
                        if (it.isNotEmpty() && !emailValid) "Invalid email" else null
                    emailValid
                },
            binding.signInPassword.textChanges()
                .debounce(500)
                .map {
                    val passwordValid = it.validate(PASSWORD_VALIDATOR)
                    binding.signInPasswordLayout.error =
                        if (it.isNotEmpty() && !passwordValid) "Invalid password (minimum 8 chars)" else null
                    passwordValid
                },
        ) { a, b ->
            a && b
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        combine(
            binding.signUpEmail.textChanges()
                .debounce(500)
                .map {
                    val emailValid = it.validate(EMAIL_VALIDATOR)
                    binding.signUpEmailLayout.error =
                        if (it.isNotEmpty() && !emailValid) "Invalid email" else null
                    emailValid
                },
            binding.signUpPassword.textChanges()
                .debounce(500)
                .map {
                    val passwordValid = it.validate(PASSWORD_VALIDATOR)
                    binding.signUpPasswordLayout.error =
                        if (it.isNotEmpty() && !passwordValid) "Invalid password (minimum 8 chars)" else null
                    passwordValid
                },
            binding.signUpConfirmPassword.textChanges()
                .debounce(500)
                .map {
                    val password = binding.signUpPassword.text.toString()
                    val passwordMatchValid = it.toString() == password
                    binding.signUpConfirmPasswordLayout.error =
                        if (password.validate(PASSWORD_VALIDATOR) && !passwordMatchValid) "Passwords don't match" else null
                    passwordMatchValid
                },
        ) { a, b, c ->
            a && b && c
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onResume() {
        super.onResume()
        toggleLightSystemBars(true)
    }
}
