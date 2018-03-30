@file:Suppress("unused")

package net.samystudio.beaver.ui.main.authenticator

import com.bluelinelabs.conductor.Controller
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.samystudio.beaver.di.key.ControllerViewModelKey
import net.samystudio.beaver.di.scope.ControllerScope
import net.samystudio.beaver.ui.base.controller.BaseControllerModule
import net.samystudio.beaver.ui.base.viewmodel.BaseControllerViewModel

@Module(includes = [BaseControllerModule::class])
abstract class AuthenticatorControllerModule {
    @Binds
    @ControllerScope
    abstract fun bindController(controller: AuthenticatorController): Controller

    @Binds
    @IntoMap
    @ControllerViewModelKey(AuthenticatorControllerViewModel::class)
    @ControllerScope
    abstract fun bindViewModel(viewModel: AuthenticatorControllerViewModel): BaseControllerViewModel
}
