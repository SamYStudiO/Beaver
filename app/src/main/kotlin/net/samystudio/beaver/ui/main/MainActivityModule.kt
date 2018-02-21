package net.samystudio.beaver.ui.main

import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.samystudio.beaver.di.scope.PerActivity
import net.samystudio.beaver.di.scope.PerFragment
import net.samystudio.beaver.ui.base.activity.BaseActivityModule

@Module(includes = [BaseActivityModule::class])
abstract class MainActivityModule
{
    @Binds
    @PerActivity
    abstract fun bindActivity(activity: MainActivity): AppCompatActivity

    @ContributesAndroidInjector(modules = [MainFragmentModule::class])
    @PerFragment
    abstract fun contributeMainFragment(): MainFragment
}
