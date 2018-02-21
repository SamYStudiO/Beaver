package net.samystudio.beaver.di.component

import dagger.BindsInstance
import dagger.Component
import net.samystudio.beaver.BeaverApplication
import net.samystudio.beaver.OkHttpGlideModule
import net.samystudio.beaver.di.module.ApplicationModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface GlideComponent
{
    fun inject(okHttpGlideModule: OkHttpGlideModule)

    @Component.Builder
    interface Builder
    {
        @BindsInstance
        fun application(application: BeaverApplication): GlideComponent.Builder

        fun build(): GlideComponent
    }
}
