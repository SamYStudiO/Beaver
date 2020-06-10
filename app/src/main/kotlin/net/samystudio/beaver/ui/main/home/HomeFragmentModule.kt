@file:Suppress("unused")

package net.samystudio.beaver.ui.main.home

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import net.samystudio.beaver.data.remote.HomeApiInterface
import net.samystudio.beaver.di.key.FragmentViewModelKey
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.fragment.BaseViewModelFragmentModule
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import retrofit2.Retrofit

@Module(includes = [BaseViewModelFragmentModule::class])
abstract class HomeFragmentModule {
    @Binds
    @FragmentScope
    abstract fun bindFragment(fragment: HomeFragment): Fragment

    @Binds
    @IntoMap
    @FragmentViewModelKey(HomeFragmentViewModel::class)
    @FragmentScope
    abstract fun bindViewModel(viewModel: HomeFragmentViewModel): BaseFragmentViewModel

    companion object {
        @Provides
        @FragmentScope
        fun provideHomeApiInterface(retrofit: Retrofit): HomeApiInterface =
            retrofit.create(HomeApiInterface::class.java)
    }
}
