package net.samystudio.beaver.ui.authenticator

import android.arch.lifecycle.LifecycleOwner
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import net.samystudio.beaver.di.key.ActivityViewModelKey
import net.samystudio.beaver.di.qualifier.ActivityContext
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ext.EmailValidator
import net.samystudio.beaver.ext.PasswordValidator
import net.samystudio.beaver.ui.base.activity.BaseActivityModule
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel

@Module(includes = [BaseActivityModule::class, AuthenticatorActivityFragmentBuilderModule::class])
abstract class AuthenticatorActivityModule
{
    @Binds
    @ActivityScope
    abstract fun bindActivity(activity: AuthenticatorActivity): AppCompatActivity

    @Binds
    @ActivityScope
    @ActivityContext
    abstract fun bindLifecycleOwner(activity: AuthenticatorActivity): LifecycleOwner

    @Binds
    @IntoMap
    @ActivityViewModelKey(AuthenticatorActivityViewModel::class)
    @ActivityScope
    abstract fun bindViewModel(viewModel: AuthenticatorActivityViewModel): BaseActivityViewModel

    @Module
    companion object
    {
        @Provides
        @ActivityScope
        @JvmStatic
        fun provideEmailValidator(): EmailValidator = EmailValidator()

        @Provides
        @ActivityScope
        @JvmStatic
        fun providePasswordValidator(): PasswordValidator = PasswordValidator()
    }
}
