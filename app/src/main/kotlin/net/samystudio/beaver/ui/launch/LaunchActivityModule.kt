package net.samystudio.beaver.ui.launch

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
abstract class LaunchActivityModule
{
    @Binds
    @ActivityScope
    abstract fun bindActivity(activity: LaunchActivity): AppCompatActivity

    @Binds
    @ActivityScope
    @ActivityLevel
    abstract fun bindLifecycleOwner(activity: LaunchActivity): LifecycleOwner

    @Binds
    @IntoMap
    @ActivityViewModelKey(LaunchActivityViewModel::class)
    @ActivityScope
    abstract fun bindViewModel(viewModel: LaunchActivityViewModel): BaseActivityViewModel
}
