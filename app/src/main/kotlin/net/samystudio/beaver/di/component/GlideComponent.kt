package net.samystudio.beaver.di.component

import dagger.Component
import net.samystudio.beaver.di.module.GlideModule
import net.samystudio.beaver.ext.OkHttpAppGlideModule
import javax.inject.Singleton

@Singleton
@Component(modules = [GlideModule::class])
interface GlideComponent
{
    fun inject(okHttpGlideModule: OkHttpAppGlideModule)

    @Component.Builder
    interface Builder
    {
        fun build(): GlideComponent
    }
}
