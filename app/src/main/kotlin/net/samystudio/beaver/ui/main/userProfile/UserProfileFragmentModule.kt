@file:Suppress("unused")

package net.samystudio.beaver.ui.main.userProfile

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import net.samystudio.beaver.data.remote.UserProfileApiInterface
import retrofit2.Retrofit

@Module
@InstallIn(ActivityComponent::class)
object UserProfileFragmentModule {
    @Provides
    @ActivityScoped
    fun provideUserProfileApiInterface(retrofit: Retrofit): UserProfileApiInterface =
        retrofit.create(UserProfileApiInterface::class.java)

}
