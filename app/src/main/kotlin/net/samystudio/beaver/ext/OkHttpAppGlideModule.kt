package net.samystudio.beaver.ext

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.Excludes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import net.samystudio.beaver.BeaverApplication
import net.samystudio.beaver.di.component.DaggerGlideComponent
import okhttp3.OkHttpClient
import java.io.InputStream
import javax.inject.Inject

@Excludes(OkHttpLibraryGlideModule::class)
@GlideModule
class OkHttpAppGlideModule : AppGlideModule()
{
    @Inject
    lateinit var okHttpClient: OkHttpClient

    init
    {
        DaggerGlideComponent.builder()
                .application(BeaverApplication.instance)
                .build()
                .inject(this)
    }

    override fun isManifestParsingEnabled() = false

    override fun registerComponents(context: Context, glide: Glide, registry: Registry)
    {
        registry.replace(GlideUrl::class.java,
                         InputStream::class.java,
                         OkHttpUrlLoader.Factory(okHttpClient))
    }
}