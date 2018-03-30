@file:Suppress("unused")

package net.samystudio.beaver.ui.base.activity

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.R
import net.samystudio.beaver.di.qualifier.ActivityContext
import net.samystudio.beaver.di.qualifier.ActivityRouteContainerId
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
    abstract fun bindViewModelFactory(
        viewModelFactory: ActivityViewModelFactory
    ): ViewModelProvider.Factory

    @Module
    companion object {
        @Provides
        @ActivityScope
        @ActivityRouteContainerId
        @IdRes
        @JvmStatic
        fun provideControllerContainerId(): Int = R.id.controller_container

        @Provides
        @ActivityScope
        @JvmStatic
        fun provideRoute(
            activity: BaseActivity<*>,
            @ActivityRouteContainerId
            controllerContainerId: Int
        ): Router =
            Conductor.attachRouter(
                activity, activity.findViewById(controllerContainerId),
                activity.saveInstanceState
            )

        @Provides
        @ActivityScope
        @JvmStatic
        fun provideViewModelProvider(
            activity: AppCompatActivity,
            viewModelFactory: ViewModelProvider.Factory
        ): ViewModelProvider =
            ViewModelProviders.of(activity, viewModelFactory)
    }
}