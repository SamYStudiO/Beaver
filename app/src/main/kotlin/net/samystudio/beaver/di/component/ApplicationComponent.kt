package net.samystudio.beaver.di.component

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import net.samystudio.beaver.BeaverApplication
import net.samystudio.beaver.data.service.ServiceBuilderModule
import net.samystudio.beaver.di.module.ApplicationModule
import net.samystudio.beaver.di.module.NetworkModule
import net.samystudio.beaver.di.module.SystemServiceModule
import net.samystudio.beaver.ui.ActivityBuilderModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, AndroidSupportInjectionModule::class,
    ActivityBuilderModule::class, ServiceBuilderModule::class, SystemServiceModule::class,
    NetworkModule::class])
interface ApplicationComponent : AndroidInjector<BeaverApplication>
{
    override fun inject(application: BeaverApplication)

    fun glideComponentBuilder(): GlideComponent.Builder

    @Component.Builder
    interface Builder
    {
        @BindsInstance
        fun application(application: BeaverApplication): Builder

        fun build(): ApplicationComponent
    }
}
