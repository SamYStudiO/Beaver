package net.samystudio.beaver.di.component

import dagger.BindsInstance
import dagger.Component
import net.samystudio.beaver.BeaverApplication
import net.samystudio.beaver.di.module.ApplicationModule
import net.samystudio.beaver.di.module.GlideModule
import net.samystudio.beaver.ext.OkHttpAppGlideModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, GlideModule::class])
interface GlideComponent
{
    fun inject(okHttpGlideModule: OkHttpAppGlideModule)

    @Component.Builder
    interface Builder
    {
        @BindsInstance
        fun application(application: BeaverApplication): GlideComponent.Builder

        fun build(): GlideComponent
    }
}
