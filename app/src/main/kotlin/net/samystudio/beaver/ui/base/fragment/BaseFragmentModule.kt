package net.samystudio.beaver.ui.base.fragment

import android.support.v4.app.Fragment
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ext.GlideApp
import net.samystudio.beaver.ext.GlideRequests

@Module
class BaseFragmentModule
{
    @Provides
    @FragmentScope
    fun provideFragmentGlideRequests(fragment: Fragment): GlideRequests = GlideApp.with(fragment)
}
