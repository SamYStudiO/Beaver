@file:Suppress("unused")

package net.samystudio.beaver.ui.base.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import net.samystudio.beaver.di.qualifier.ActivityContext
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
    @ActivityContext
    abstract fun bindViewModelFactory(viewModelFactory: ActivityViewModelFactory): ViewModelProvider.Factory
}