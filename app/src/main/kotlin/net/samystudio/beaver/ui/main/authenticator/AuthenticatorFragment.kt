@file:Suppress("ProtectedInFinal")

package net.samystudio.beaver.ui.main.authenticator

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import kotlinx.android.synthetic.main.fragment_authenticator.*
import net.samystudio.beaver.R
import net.samystudio.beaver.data.local.SharedPreferencesHelper
import net.samystudio.beaver.ext.*
import net.samystudio.beaver.ui.base.fragment.BaseDataPushFragment
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthenticatorFragment : BaseDataPushFragment<AuthenticatorFragmentViewModel>() {
    override val viewModel by viewModels<AuthenticatorFragmentViewModel>()
    override val layoutViewRes: Int = R.layout.fragment_authenticator
    private var disposables: CompositeDisposable? = null
    @Inject
    protected lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sign_in_email.setText(sharedPreferencesHelper.accountName.get())

        viewModel.signInVisibility.observe(this, Observer { sign_in_layout.isVisible = it })
        viewModel.signUpVisibility.observe(this, Observer { sign_up_layout.isVisible = it })

        viewModel.addUserFlow(
            sign_in.clicks()
                .map {
                    AuthenticatorUserFlow.SignIn(
                        sign_in_email.text.toString(),
                        sign_in_password.text.toString()
                    )
                })
        viewModel.addUserFlow(
            sign_up.clicks()
                .map {
                    AuthenticatorUserFlow.SignUp(
                        sign_up_email.text.toString(),
                        sign_up_password.text.toString()
                    )
                })

        disposables = CompositeDisposable()

        disposables?.add(
            Observables
                .combineLatest(
                    sign_in_email.textChanges()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { t ->
                            val emailValid = t.validate(EMAIL_VALIDATOR)
                            sign_in_email_layout.error =
                                if (t.isNotEmpty() && !emailValid) "Invalid email" else null
                            emailValid
                        },
                    sign_in_password.textChanges()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { t ->
                            val passwordValid = t.validate(PASSWORD_VALIDATOR)
                            sign_in_password_layout.error =
                                if (t.isNotEmpty() && !passwordValid) "Invalid password (minimum 8 chars)" else null
                            passwordValid
                        }
                ) { t1, t2 -> t1 && t2 }
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(false)
                .subscribe { sign_in.isEnabled = it })

        disposables?.add(
            Observables
                .combineLatest(
                    sign_up_email.textChanges()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { t ->
                            val emailValid = t.validate(EMAIL_VALIDATOR)
                            sign_up_email_layout.error =
                                if (t.isNotEmpty() && !emailValid) "Invalid email" else null
                            emailValid
                        },
                    sign_up_password.textChanges()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { t ->
                            val passwordValid = t.validate(PASSWORD_VALIDATOR)
                            sign_up_password_layout.error =
                                if (t.isNotEmpty() && !passwordValid) "Invalid password (minimum 8 chars)" else null
                            passwordValid
                        },
                    sign_up_confirm_password.textChanges()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { t ->
                            val password = sign_up_password.text.toString()
                            val passwordMatchValid = t.toString() == password
                            sign_up_confirm_password_layout.error =
                                if (password.validate(PASSWORD_VALIDATOR) && !passwordMatchValid) "Passwords don't match" else null
                            passwordMatchValid
                        }
                ) { t1, t2, t3 -> t1 && t2 && t3 }
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(false)
                .subscribe { sign_up.isEnabled = it })
    }

    override fun dataPushStart() {
        // TODO show loader
    }

    override fun dataPushSuccess() {
    }

    override fun dataPushError(throwable: Throwable) {
        getGenericErrorDialog(requireContext()).showNow(requireFragmentManager(), getMethodTag())
    }

    override fun dataPushTerminate() {
        // TODO hide loader
    }

    override fun onDestroyView() {
        super.onDestroyView()

        disposables?.dispose()
    }
}