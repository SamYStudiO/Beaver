package net.samystudio.beaver.ui.base.activity

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.annotation.IdRes
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.R
import net.samystudio.beaver.di.qualifier.ActivityContext
import net.samystudio.beaver.di.qualifier.ActivityLevel
import net.samystudio.beaver.di.qualifier.FragmentContainerViewId
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ext.GlideApp
import net.samystudio.beaver.ext.GlideRequests
import net.samystudio.beaver.ui.base.viewmodel.ActivityViewModelFactory

@Module
abstract class BaseActivityModule
{
    @Binds
    @ActivityScope
    @ActivityContext
    abstract fun bindActivityContext(activity: AppCompatActivity): Context

    @Binds
    @ActivityScope
    @ActivityLevel
    abstract fun bindViewModelFactory(viewModelFactory: ActivityViewModelFactory): ViewModelProvider.Factory

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
        @ActivityLevel
        @JvmStatic
        fun provideViewModelProvider(activity: AppCompatActivity,
                                     @ActivityLevel
                                     viewModelFactory: ViewModelProvider.Factory): ViewModelProvider =
            ViewModelProviders.of(activity, viewModelFactory)

        @Provides
        @FragmentScope
        @ActivityLevel
        @JvmStatic
        fun provideGlideRequests(activity: AppCompatActivity): GlideRequests =
            GlideApp.with(activity)
    }
}