@file:Suppress("unused")

package net.samystudio.beaver.ui.main

import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.samystudio.beaver.di.key.ActivityViewModelKey
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.FragmentBuilderModule
import net.samystudio.beaver.ui.base.activity.BaseActivityModule
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel

@Module(includes = [BaseActivityModule::class, FragmentBuilderModule::class])
abstract class MainActivityModule {
    @Binds
    @ActivityScope
    abstract fun bindActivity(activity: MainActivity): AppCompatActivity

    @Binds
    @IntoMap
    @ActivityViewModelKey(MainActivityViewModel::class)
    @ActivityScope
    abstract fun bindViewModel(viewModel: MainActivityViewModel): BaseActivityViewModel
}
