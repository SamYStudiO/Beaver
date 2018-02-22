package net.samystudio.beaver.di.component

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import net.samystudio.beaver.BeaverApplication
import net.samystudio.beaver.di.module.ApiModule
import net.samystudio.beaver.di.module.ApplicationModule
import net.samystudio.beaver.di.module.SystemServiceModule
import net.samystudio.beaver.ui.ActivityBuildersModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, AndroidSupportInjectionModule::class,
    ActivityBuildersModule::class, SystemServiceModule::class, ApiModule::class])
interface ApplicationComponent : AndroidInjector<BeaverApplication>
{
    override fun inject(application: BeaverApplication)

    @Component.Builder
    interface Builder
    {
        @BindsInstance
        fun application(application: BeaverApplication): Builder

        fun build(): ApplicationComponent
    }
}
