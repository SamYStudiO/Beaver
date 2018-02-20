package net.samystudio.beaver.di.component

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import net.samystudio.beaver.HighwayApplication
import net.samystudio.beaver.di.module.ApplicationModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent : AndroidInjector<HighwayApplication>
{
    override fun inject(application: HighwayApplication)

    @Component.Builder
    interface Builder
    {
        @BindsInstance
        fun application(application: HighwayApplication): Builder

        fun build(): ApplicationComponent
    }
}
