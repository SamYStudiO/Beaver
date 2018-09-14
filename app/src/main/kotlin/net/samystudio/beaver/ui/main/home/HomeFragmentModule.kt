@file:Suppress("unused")

package net.samystudio.beaver.ui.main.home

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import net.samystudio.beaver.data.remote.api.HomeApiInterface
import net.samystudio.beaver.di.key.FragmentViewModelKey
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ext.EmailValidator
import net.samystudio.beaver.ext.PasswordValidator
import net.samystudio.beaver.ui.base.fragment.BaseFragmentModule
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import retrofit2.Retrofit

@Module(includes = [BaseFragmentModule::class])
abstract class HomeFragmentModule {
    @Binds
    @FragmentScope
    abstract fun bindFragment(fragment: HomeFragment): androidx.fragment.app.Fragment

    @Binds
    @IntoMap
    @FragmentViewModelKey(HomeFragmentViewModel::class)
    @FragmentScope
    abstract fun bindViewModel(viewModel: HomeFragmentViewModel): BaseFragmentViewModel

    @Module
    companion object {
        @Provides
        @FragmentScope
        @JvmStatic
        fun provideHomeApiInterface(retrofit: Retrofit): HomeApiInterface =
            retrofit.create(HomeApiInterface::class.java)

        @Provides
        @ActivityScope
        @JvmStatic
        fun provideEmailValidator(): EmailValidator = EmailValidator()

        @Provides
        @ActivityScope
        @JvmStatic
        fun providePasswordValidator(): PasswordValidator = PasswordValidator()
    }
}
