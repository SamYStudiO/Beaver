@file:Suppress("unused")

package net.samystudio.beaver.ui.main.authenticator

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.samystudio.beaver.di.key.FragmentViewModelKey
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.fragment.BaseFragmentModule
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel

@Module(includes = [BaseFragmentModule::class])
abstract class AuthenticatorFragmentModule {
    @Binds
    @FragmentScope
    abstract fun bindFragment(fragment: AuthenticatorFragment): androidx.fragment.app.Fragment

    @Binds
    @IntoMap
    @FragmentViewModelKey(AuthenticatorFragmentViewModel::class)
    @FragmentScope
    abstract fun bindViewModel(viewModel: AuthenticatorFragmentViewModel): BaseFragmentViewModel
}
