package net.samystudio.beaver.ui.main

import android.arch.lifecycle.LifecycleOwner
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.samystudio.beaver.di.key.ActivityViewModelKey
import net.samystudio.beaver.di.qualifier.ActivityLevel
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.base.activity.BaseActivityModule
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel

@Module(includes = [BaseActivityModule::class, FragmentBuildersModule::class])
abstract class MainActivityModule
{
    @Binds
    @ActivityScope
    abstract fun bindActivity(activity: MainActivity): AppCompatActivity

    @Binds
    @ActivityScope
    @ActivityLevel
    abstract fun bindLifecycleOwner(activity: MainActivity): LifecycleOwner

    @Binds
    @IntoMap
    @ActivityViewModelKey(MainActivityViewModel::class)
    @ActivityScope
    abstract fun bindViewModel(viewModel: MainActivityViewModel): BaseActivityViewModel
}
