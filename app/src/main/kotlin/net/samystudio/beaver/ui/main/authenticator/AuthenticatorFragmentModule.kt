@file:Suppress("unused")

package net.samystudio.beaver.ui.main.authenticator

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.samystudio.beaver.di.key.FragmentViewModelKey
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.fragment.BaseViewModelFragmentModule
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel

@Module(includes = [BaseViewModelFragmentModule::class])
abstract class AuthenticatorFragmentModule {
    @Binds
    @FragmentScope
    abstract fun bindFragment(fragment: AuthenticatorFragment): Fragment

    @Binds
    @IntoMap
    @FragmentViewModelKey(AuthenticatorFragmentViewModel::class)
    @FragmentScope
    abstract fun bindViewModel(viewModel: AuthenticatorFragmentViewModel): BaseFragmentViewModel
}
