@file:Suppress("ProtectedInFinal")

package net.samystudio.beaver.ui.main.authenticator

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

        binding.login.setOnClickListener {
            viewModel.login(
                binding.loginEmail.text.toString(),
                binding.loginPassword.text.toString()
            )
        }
        binding.register.setOnClickListener {
            viewModel.register(
                binding.registerEmail.text.toString(),
                binding.registerPassword.text.toString()
            )
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.defaultAccountName.onEach {
                    binding.loginEmail.setText(it)
                }.launchIn(this)

                viewModel.defaultAccountPassword.onEach {
                    binding.loginPassword.setText(it)
                }.launchIn(this)

                viewModel.accountName.onEach {
                    if (it.isNotBlank()) {
                        binding.loginEmail.setText(it)
                        binding.loginPassword.setText("")
                    }
                }.launchIn(this)

                viewModel.loginState.onEach {
                    it.handleStatesFromFragmentWithLoaderDialog(this@AuthenticatorFragment) {
                        // TODO error handling
                        findNavController().popBackStack()
                    }
                }.launchIn(this)

                viewModel.registerState.onEach {
                    it.handleStatesFromFragmentWithLoaderDialog(this@AuthenticatorFragment) {
                        // TODO error handling
                        findNavController().popBackStack()
                    }
                }.launchIn(this)

            }
        }

        combine(
            binding.registerEmail.textChanges()
                .debounce(500)
                .map {
                    val emailValid = it.validate(EMAIL_VALIDATOR)
                    binding.signUpEmailLayout.error =
                        if (it.isNotEmpty() && !emailValid) getString(R.string.error_email) else null
                    emailValid
                },
            binding.registerPassword.textChanges()
                .debounce(500)
                .map {
                    val passwordValid = it.validate(PASSWORD_VALIDATOR)
                    binding.signUpPasswordLayout.error =
                        if (it.isNotEmpty() && !passwordValid) getString(R.string.error_password) else null
                    passwordValid
                },
            binding.signUpConfirmPassword.textChanges()
                .debounce(500)
                .map {
                    val password = binding.registerPassword.text.toString()
                    val passwordMatchValid = it.toString() == password
                    binding.signUpConfirmPasswordLayout.error =
                        if (password.validate(PASSWORD_VALIDATOR) && !passwordMatchValid) getString(
                            R.string.error_password_match
                        ) else null
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
