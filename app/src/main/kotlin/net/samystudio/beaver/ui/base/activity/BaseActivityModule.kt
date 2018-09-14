@file:Suppress("unused")

package net.samystudio.beaver.ui.base.activity

import android.content.Context
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.R
import net.samystudio.beaver.di.qualifier.ActivityContext
import net.samystudio.beaver.di.qualifier.NavHostId
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.common.viewmodel.factory.ActivityViewModelFactory

@Module
abstract class BaseActivityModule {
    @Binds
    @ActivityScope
    @ActivityContext
    abstract fun bindActivityContext(activity: AppCompatActivity): Context

    @Binds
    @ActivityScope
    abstract fun bindViewModelFactory(viewModelFactory: ActivityViewModelFactory): ViewModelProvider.Factory

    @Module
    companion object {
        @Provides
        @ActivityScope
        @NavHostId
        @IdRes
        @JvmStatic
        fun provideNavHostId(): Int = R.id.nav_host

        @Provides
        @ActivityScope
        @JvmStatic
        fun provideNavController(activity: AppCompatActivity, @NavHostId navHostId: Int): NavController =
            activity.findNavController(navHostId)

        @Provides
        @ActivityScope
        @ActivityContext
        @JvmStatic
        fun provideViewModelProvider(
            activity: AppCompatActivity,
            viewModelFactory: ViewModelProvider.Factory
        ): ViewModelProvider = ViewModelProviders.of(activity, viewModelFactory)
    }
}