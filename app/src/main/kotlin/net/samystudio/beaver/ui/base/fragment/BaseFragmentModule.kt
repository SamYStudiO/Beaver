package net.samystudio.beaver.ui.base.fragment

import dagger.Module
import dagger.Provides
import net.samystudio.beaver.di.scope.PerFragment
import net.samystudio.beaver.ext.GlideApp
import net.samystudio.beaver.ext.GlideRequests

@Module
class BaseFragmentModule
{
    @Provides
    @PerFragment
    fun provideActivityGlideRequests(fragment: BaseFragment): GlideRequests = GlideApp.with(fragment)
}
