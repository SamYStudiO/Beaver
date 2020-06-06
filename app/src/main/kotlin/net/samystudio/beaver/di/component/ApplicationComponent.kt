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
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: BeaverApplication): ApplicationComponent
    }
}