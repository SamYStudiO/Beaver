@file:Suppress("unused")

package net.samystudio.beaver.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.main.MainActivity
import net.samystudio.beaver.ui.main.MainActivityModule

/**
 * Dagger injection for all activities. All [net.samystudio.beaver.ui.base.activity.BaseActivity]
 * must be referenced here.
 */
@Module
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    @ActivityScope
    abstract fun contributeMainActivity(): MainActivity
}