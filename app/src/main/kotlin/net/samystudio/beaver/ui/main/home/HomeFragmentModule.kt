@file:Suppress("unused")

package net.samystudio.beaver.ui.main.home

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import net.samystudio.beaver.data.remote.HomeApiInterface
import retrofit2.Retrofit

@Module
@InstallIn(ActivityComponent::class)
object HomeFragmentModule {
    @Provides
    @ActivityScoped
    fun provideHomeApiInterface(retrofit: Retrofit): HomeApiInterface =
        retrofit.create(HomeApiInterface::class.java)

}
