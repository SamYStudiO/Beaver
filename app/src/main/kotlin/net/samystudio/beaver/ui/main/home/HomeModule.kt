package net.samystudio.beaver.ui.main.home

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import net.samystudio.beaver.data.remote.HomeApiInterface
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
object HomeModule {
    @Provides
    @ActivityRetainedScoped
    fun provideHomeApiInterface(retrofit: Retrofit): HomeApiInterface =
        retrofit.create(HomeApiInterface::class.java)
}