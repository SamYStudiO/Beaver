@file:Suppress("unused")

package net.samystudio.beaver.ui.main.home

import com.bluelinelabs.conductor.Controller
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import net.samystudio.beaver.data.remote.api.HomeApiInterface
import net.samystudio.beaver.di.key.ControllerViewModelKey
import net.samystudio.beaver.di.scope.ControllerScope
import net.samystudio.beaver.ext.EmailValidator
import net.samystudio.beaver.ext.PasswordValidator
import net.samystudio.beaver.ui.base.controller.BaseControllerModule
import net.samystudio.beaver.ui.base.viewmodel.BaseControllerViewModel
import retrofit2.Retrofit

@Module(includes = [BaseControllerModule::class])
abstract class HomeControllerModule {
    @Binds
    @ControllerScope
    abstract fun bindController(controller: HomeController): Controller

    @Binds
    @IntoMap
    @ControllerViewModelKey(HomeControllerViewModel::class)
    @ControllerScope
    abstract fun bindViewModel(viewModel: HomeControllerViewModel): BaseControllerViewModel

    @Module
    companion object {
        @Provides
        @ControllerScope
        @JvmStatic
        fun provideHomeApiInterface(retrofit: Retrofit): HomeApiInterface =
            retrofit.create(HomeApiInterface::class.java)

        @Provides
        @ControllerScope
        @JvmStatic
        fun provideEmailValidator(): EmailValidator = EmailValidator()

        @Provides
        @ControllerScope
        @JvmStatic
        fun providePasswordValidator(): PasswordValidator = PasswordValidator()
    }
}