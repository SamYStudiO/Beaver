@file:Suppress("ProtectedInFinal")

package net.samystudio.beaver.ui.main.authenticator

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import net.samystudio.beaver.data.local.SharedPreferencesHelper
import net.samystudio.beaver.databinding.FragmentAuthenticatorBinding
import net.samystudio.beaver.ext.*
import net.samystudio.beaver.ui.base.fragment.BaseDataPushFragment
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthenticatorFragment :
    BaseDataPushFragment<FragmentAuthenticatorBinding, AuthenticatorFragmentViewModel>() {
    @Inject
    protected lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    override val binding: FragmentAuthenticatorBinding by viewBinding { inflater, container ->
        FragmentAuthenticatorBinding.inflate(inflater, container, false)
    }
    override val viewModel by viewModels<AuthenticatorFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInEmail.setText(sharedPreferencesHelper.accountName.get())

        viewModel.signInVisibility.observe(
            viewLifecycleOwner,
            Observer { binding.signInLayout.isVisible = it })
        viewModel.signUpVisibility.observe(
            viewLifecycleOwner,
            Observer { binding.signUpEmailLayout.isVisible = it })

        viewModel.addUserFlow(
            binding.signIn.clicks()
                .map {
                    AuthenticatorUserFlow.SignIn(
                        binding.signInEmail.text.toString(),
                        binding.signInPassword.text.toString()
                    )
                })
        viewModel.addUserFlow(
            binding.signUp.clicks()
                .map {
                    AuthenticatorUserFlow.SignUp(
                        binding.signInEmail.text.toString(),
                        binding.signInPassword.text.toString()
                    )
                })

        destroyViewDisposable?.add(
            Observables
                .combineLatest(
                    binding.signInEmail.textChanges()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { t ->
                            val emailValid = t.validate(EMAIL_VALIDATOR)
                            binding.signInEmailLayout.error =
                                if (t.isNotEmpty() && !emailValid) "Invalid email" else null
                            emailValid
                        },
                    binding.signInPassword.textChanges()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { t ->
                            val passwordValid = t.validate(PASSWORD_VALIDATOR)
                            binding.signInPasswordLayout.error =
                                if (t.isNotEmpty() && !passwordValid) "Invalid password (minimum 8 chars)" else null
                            passwordValid
                        }
                ) { t1, t2 -> t1 && t2 }
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(false)
                .subscribe { binding.signIn.isEnabled = it })

        destroyViewDisposable?.add(
            Observables
                .combineLatest(
                    binding.signUpEmail.textChanges()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { t ->
                            val emailValid = t.validate(EMAIL_VALIDATOR)
                            binding.signUpEmailLayout.error =
                                if (t.isNotEmpty() && !emailValid) "Invalid email" else null
                            emailValid
                        },
                    binding.signUpPassword.textChanges()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { t ->
                            val passwordValid = t.validate(PASSWORD_VALIDATOR)
                            binding.signUpPasswordLayout.error =
                                if (t.isNotEmpty() && !passwordValid) "Invalid password (minimum 8 chars)" else null
                            passwordValid
                        },
                    binding.signUpConfirmPassword.textChanges()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { t ->
                            val password = binding.signUpPassword.text.toString()
                            val passwordMatchValid = t.toString() == password
                            binding.signUpConfirmPasswordLayout.error =
                                if (password.validate(PASSWORD_VALIDATOR) && !passwordMatchValid) "Passwords don't match" else null
                            passwordMatchValid
                        }
                ) { t1, t2, t3 -> t1 && t2 && t3 }
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(false)
                .subscribe { binding.signUp.isEnabled = it })
    }

    override fun dataPushStart() {
        // TODO Show loader.
    }

    override fun dataPushSuccess() {
    }

    override fun dataPushError(throwable: Throwable) {
        getGenericErrorDialog().showNow(parentFragmentManager, getMethodTag())
    }

    override fun dataPushTerminate() {
        // TODO Hide loader.
    }
}