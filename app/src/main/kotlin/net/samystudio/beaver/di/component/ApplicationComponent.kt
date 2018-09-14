package net.samystudio.beaver.di.component

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import net.samystudio.beaver.BeaverApplication
import net.samystudio.beaver.di.module.ApplicationModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent : AndroidInjector<BeaverApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: BeaverApplication): Builder

        fun build(): ApplicationComponent
    }
}