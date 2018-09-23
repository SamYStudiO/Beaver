@file:Suppress("unused")

package net.samystudio.beaver.ui.main.userProfile

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import net.samystudio.beaver.data.remote.api.UserProfileApiInterface
import net.samystudio.beaver.di.key.FragmentViewModelKey
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.fragment.BaseFragmentModule
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import retrofit2.Retrofit

@Module(includes = [BaseFragmentModule::class])
abstract class UserProfileFragmentModule {
    @Binds
    @FragmentScope
    abstract fun bindFragment(fragment: UserProfileFragment): Fragment

    @Binds
    @IntoMap
    @FragmentViewModelKey(UserProfileFragmentViewModel::class)
    @FragmentScope
    abstract fun bindViewModel(viewModel: UserProfileFragmentViewModel): BaseFragmentViewModel

    @Module
    companion object {
        @Provides
        @FragmentScope
        @JvmStatic
        fun provideUserProfileApiInterface(retrofit: Retrofit): UserProfileApiInterface =
            retrofit.create(UserProfileApiInterface::class.java)
    }
}
