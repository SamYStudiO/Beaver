package net.samystudio.beaver.di.component

import dagger.Subcomponent
import net.samystudio.beaver.di.module.GlideModule
import net.samystudio.beaver.di.scope.GlideScope
import net.samystudio.beaver.ext.OkHttpAppGlideModule

/**
 * Glide Dagger component to inject its own [okhttp3.OkHttpClient].
 *
 * @see GlideModule
 */
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
