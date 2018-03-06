package net.samystudio.beaver.ui.main.home

import android.arch.lifecycle.LifecycleOwner
import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import net.samystudio.beaver.data.remote.HomeApiInterface
import net.samystudio.beaver.di.key.FragmentViewModelKey
import net.samystudio.beaver.di.qualifier.FragmentLevel
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.fragment.BaseDataFragmentModule
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import retrofit2.Retrofit

@Module(includes = [BaseDataFragmentModule::class])
abstract class HomeFragmentModule
{
    @Binds
    @FragmentScope
    abstract fun bindFragment(fragment: HomeFragment): Fragment

    @Binds
    @FragmentScope
    @FragmentLevel
    abstract fun bindLifecycleOwner(fragment: HomeFragment): LifecycleOwner

    @Binds
    @IntoMap
    @FragmentViewModelKey(HomeFragmentViewModel::class)
    @FragmentScope
    abstract fun bindViewModel(viewModel: HomeFragmentViewModel): BaseFragmentViewModel

    @Module
    companion object
    {
        @Provides
        @FragmentScope
        @JvmStatic
        fun provideHomeApiInterface(retrofit: Retrofit): HomeApiInterface =
            retrofit.create(HomeApiInterface::class.java)
    }
}
