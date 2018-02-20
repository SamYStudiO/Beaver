package net.samystudio.beaver.ui.launch

import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.samystudio.beaver.di.scope.PerActivity
import net.samystudio.beaver.di.scope.PerFragment
import net.samystudio.beaver.ui.base.activity.BaseActivityModule
import net.samystudio.beaver.ui.base.fragment.BaseFragmentModule

@Module(includes = [BaseActivityModule::class])
abstract class LaunchActivityModule
{
    @Binds
    @PerActivity
    abstract fun bindActivity(activity: LaunchActivity): AppCompatActivity

    @ContributesAndroidInjector(modules = [BaseFragmentModule::class])
    @PerFragment
    abstract fun contributeLaunchFragment(): LaunchFragment
}
