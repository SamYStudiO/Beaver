package net.samystudio.beaver.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import net.samystudio.beaver.data.remote.HomeApiInterface
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelDataModule {
    @Provides
    @ViewModelScoped
    fun provideHomeApiInterface(retrofit: Retrofit): HomeApiInterface =
        retrofit.create(HomeApiInterface::class.java)
}
