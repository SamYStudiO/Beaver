package net.samystudio.beaver.ui.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.common.dialog.AlertDialog
import net.samystudio.beaver.ui.common.dialog.AlertDialogModule
import net.samystudio.beaver.ui.main.home.HomeFragment
import net.samystudio.beaver.ui.main.home.HomeFragmentModule

@Module
abstract class MainActivityFragmentBuilderModule
{
    @ContributesAndroidInjector(modules = [HomeFragmentModule::class])
    @FragmentScope
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector(modules = [AlertDialogModule::class])
    @FragmentScope
    abstract fun contributeAlertDialog(): AlertDialog
}
