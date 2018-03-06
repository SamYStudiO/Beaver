package net.samystudio.beaver.di.component

import dagger.Subcomponent
import net.samystudio.beaver.di.module.GlideModule
import net.samystudio.beaver.di.scope.GlideScope
import net.samystudio.beaver.ext.OkHttpAppGlideModule

@GlideScope
@Subcomponent(modules = [GlideModule::class])
interface GlideComponent
{
    fun inject(okHttpGlideModule: OkHttpAppGlideModule)

    @Subcomponent.Builder
    interface Builder
    {
        fun build(): GlideComponent
    }
}
