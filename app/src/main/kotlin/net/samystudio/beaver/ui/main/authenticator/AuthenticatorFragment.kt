@file:Suppress("ProtectedInFinal")

package net.samystudio.beaver.ui.main.authenticator

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import net.samystudio.beaver.R
import net.samystudio.beaver.data.handleStatesFromFragmentWithLoaderDialog
import net.samystudio.beaver.data.local.SharedPreferencesHelper
import net.samystudio.beaver.databinding.FragmentAuthenticatorBinding
import net.samystudio.beaver.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticatorFragment : Fragment(R.layout.fragment_authenticator) {
    private val binding by viewBinding { FragmentAuthenticatorBinding.bind(it) }
    private val viewModel by viewModels<AuthenticatorFragmentViewModel>()
    private var compositeDisposable: CompositeDisposable? = null

    @Inject
    protected lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compositeDisposable = CompositeDisposable()
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

        binding.signInEmail.setText(sharedPreferencesHelper.accountName.get())

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

        compositeDisposable?.add(
            Observable
                .combineLatest(
                    binding.signInEmail.textChanges()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { t ->
                            val emailValid = t.validate(EMAIL_VALIDATOR)
                            binding.signInEmailLayout.error =
                                if (t.isNotEmpty() && !emailValid) getString(R.string.error_email) else null
                            emailValid
                        },
                    binding.signInPassword.textChanges()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { t ->
                            val passwordValid = t.validate(PASSWORD_VALIDATOR)
                            binding.signInPasswordLayout.error =
                                if (t.isNotEmpty() && !passwordValid) getString(R.string.error_password) else null
                            passwordValid
                        }
                ) { t1, t2 -> t1 && t2 }
                .observeOn(AndroidSchedulers.mainThread())
                .startWithItem(false)
                .subscribe { binding.signIn.isEnabled = it }
        )

        compositeDisposable?.add(
            Observable
                .combineLatest(
                    binding.signUpEmail.textChanges()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { t ->
                            val emailValid = t.validate(EMAIL_VALIDATOR)
                            binding.signUpEmailLayout.error =
                                if (t.isNotEmpty() && !emailValid) getString(R.string.error_email) else null
                            emailValid
                        },
                    binding.signUpPassword.textChanges()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { t ->
                            val passwordValid = t.validate(PASSWORD_VALIDATOR)
                            binding.signUpPasswordLayout.error =
                                if (t.isNotEmpty() && !passwordValid) getString(R.string.error_password) else null
                            passwordValid
                        },
                    binding.signUpConfirmPassword.textChanges()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { t ->
                            val password = binding.signUpPassword.text.toString()
                            val passwordMatchValid = t.toString() == password
                            binding.signUpConfirmPasswordLayout.error =
                                if (password.validate(PASSWORD_VALIDATOR) && !passwordMatchValid) getString(R.string.error_password_match) else null
                            passwordMatchValid
                        }
                ) { t1, t2, t3 -> t1 && t2 && t3 }
                .observeOn(AndroidSchedulers.mainThread())
                .startWithItem(false)
                .subscribe { binding.signUp.isEnabled = it }
        )

        viewModel.signLiveData.observe(
            viewLifecycleOwner,
            {
                it.handleStatesFromFragmentWithLoaderDialog(
                    this,
                    complete = { findNavController().popBackStack() },
                )
            }
        )
    }

    override fun onResume() {
        super.onResume()
        toggleLightSystemBars(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable?.dispose()
        compositeDisposable = null
    }
}
