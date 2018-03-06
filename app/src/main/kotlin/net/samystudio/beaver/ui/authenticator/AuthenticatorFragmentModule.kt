package net.samystudio.beaver.ui.authenticator

import android.arch.lifecycle.LifecycleOwner
import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.samystudio.beaver.di.key.FragmentViewModelKey
import net.samystudio.beaver.di.qualifier.FragmentContext
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.fragment.BaseDataFragmentModule
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel

@Module(includes = [BaseDataFragmentModule::class])
abstract class AuthenticatorFragmentModule
{
    @Binds
    @FragmentScope
    abstract fun bindFragment(fragment: AuthenticatorFragment): Fragment

    @Binds
    @FragmentScope
    @FragmentContext
    abstract fun bindLifecycleOwner(fragment: AuthenticatorFragment): LifecycleOwner

    @Binds
    @IntoMap
    @FragmentViewModelKey(AuthenticatorFragmentViewModel::class)
    @FragmentScope
    abstract fun bindViewModel(viewModel: AuthenticatorFragmentViewModel): BaseFragmentViewModel
}
