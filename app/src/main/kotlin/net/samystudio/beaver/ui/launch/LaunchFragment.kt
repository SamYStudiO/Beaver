package net.samystudio.beaver.ui.launch

import android.os.Bundle
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_launch.*
import net.samystudio.beaver.GlideApp
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import timber.log.Timber

class LaunchFragment : BaseFragment()
{
    override fun init(savedInstanceState: Bundle?)
    {
        Timber.d("init: %s", fragmentNavigationManager)

        GlideApp
                .with(this)
                .load("https://www.gettyimages.ca/gi-resources/images/Homepage/Hero/UK/CMS_Creative_164657191_Kingfisher.jpg")
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(
                        true))
                .into(image)
    }

    override fun getLayoutViewRes(): Int = R.layout.fragment_launch

    override fun getDefaultTitle(): String = ""
}