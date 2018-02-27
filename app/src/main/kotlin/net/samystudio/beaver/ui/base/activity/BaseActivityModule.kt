package net.samystudio.beaver.ui.base.activity

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.R
import net.samystudio.beaver.di.qualifier.ActivityContext
import net.samystudio.beaver.di.qualifier.FragmentContainerViewId
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.common.model.ViewModelFactory

@Module
abstract class BaseActivityModule
{
    @Binds
    @ActivityContext
    @ActivityScope
    abstract fun bindActivityContext(activity: AppCompatActivity): Context

    @Binds
    @FragmentScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Module
    companion object
    {
        @Provides
        @ActivityScope
        @FragmentContainerViewId
        @IdRes
        @JvmStatic
        fun provideFragmentContainerViewId(): Int = R.id.fragment_container

        @Provides
        @ActivityScope
        @JvmStatic
        fun provideFragmentManager(activity: AppCompatActivity): FragmentManager =
            activity.supportFragmentManager

        @Provides
        @ActivityScope
        @JvmStatic
        fun provideViewModelProviders(fragment: Fragment,
                                      viewModelFactory: ViewModelFactory): ViewModelProvider =
            ViewModelProviders.of(fragment, viewModelFactory)
    }
}