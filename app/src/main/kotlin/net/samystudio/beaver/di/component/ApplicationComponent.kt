package net.samystudio.beaver.di.component

import com.ivianuu.contributer.annotations.AndroidInjectorKeyRegistry
import com.ivianuu.contributer.conductor.ControllerKey
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import net.samystudio.beaver.BeaverApplication
import net.samystudio.beaver.di.module.ApplicationModule
import javax.inject.Singleton

@AndroidInjectorKeyRegistry(keys = [ControllerKey::class])
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