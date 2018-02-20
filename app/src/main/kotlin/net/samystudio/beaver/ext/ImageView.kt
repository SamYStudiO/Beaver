@file:Suppress("unused")

package net.samystudio.beaver.ext

import android.widget.ImageView
import net.samystudio.beaver.utils.RxPicasso

fun ImageView.load(picasso: Picasso, request: RequestCreator) =
        RxPicasso.withImageView(picasso, request, this)
