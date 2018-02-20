package net.samystudio.beaver.ui.base.activity

import android.content.Context
import android.support.annotation.IdRes
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.R
import net.samystudio.beaver.di.qualifier.ActivityContext
import net.samystudio.beaver.di.qualifier.FragmentContainerViewId
import net.samystudio.beaver.di.scope.PerActivity
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager

@Module
abstract class BaseActivityModule
{
    @Binds
    @ActivityContext
    @PerActivity
    abstract fun bindActivityContext(activity: AppCompatActivity): Context

    @Module
    companion object
    {
        @Provides
        @PerActivity
        @FragmentContainerViewId
        @IdRes
        @JvmStatic
        fun provideFragmentContainerViewId(): Int = R.id.fragment_container

        @Provides
        @PerActivity
        @JvmStatic
        fun provideFragmentManager(activity: AppCompatActivity): FragmentManager =
                activity.supportFragmentManager

        @Provides
        @PerActivity
        @JvmStatic
        fun provideFragmentNavigationManager(@ActivityContext
                                             context: Context,
                                             fragmentManager: FragmentManager,
                                             @FragmentContainerViewId
                                             fragmentContainerViewId: Int) =
                FragmentNavigationManager(
                        context,
                        fragmentManager,
                        fragmentContainerViewId)
    }
}