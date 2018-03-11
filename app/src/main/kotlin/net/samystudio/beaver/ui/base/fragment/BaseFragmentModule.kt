@file:Suppress("unused")

package net.samystudio.beaver.ui.base.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.di.qualifier.FragmentContainerViewId
import net.samystudio.beaver.di.qualifier.FragmentContext
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.viewmodel.factory.FragmentViewModelFactory
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager

@Module
abstract class BaseFragmentModule
{
    @Binds
    @FragmentScope
    @FragmentContext
    abstract fun bindViewModelFactory(viewModelFactory: FragmentViewModelFactory): ViewModelProvider.Factory

    @Module
    companion object
    {
        @Provides
        @FragmentScope
        @FragmentContext
        @JvmStatic
        fun provideChildFragmentManager(fragment: Fragment): FragmentManager =
            fragment.childFragmentManager

        @Provides
        @FragmentScope
        @FragmentContext
        @JvmStatic
        fun provideChildFragmentNavigationManager(activity: AppCompatActivity,
                                                  @FragmentContext
                                                  fragmentManager: FragmentManager,
                                                  @FragmentContainerViewId
                                                  @IdRes
                                                  fragmentContainerViewId: Int): FragmentNavigationManager =
            FragmentNavigationManager(activity, fragmentManager, fragmentContainerViewId)

        @Provides
        @FragmentScope
        @FragmentContext
        @JvmStatic
        fun provideViewModelProvider(fragment: Fragment,
                                     @FragmentContext
                                     viewModelFactory: ViewModelProvider.Factory): ViewModelProvider =
            ViewModelProviders.of(fragment, viewModelFactory)

        @Provides
        @FragmentScope
        @FragmentContext
        @JvmStatic
        fun provideGlideRequests(fragment: Fragment): RequestManager =
            Glide.with(fragment)
    }
}
