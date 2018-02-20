@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.StringDef
import android.view.View
import android.widget.ImageView
import io.reactivex.*

object RxPicasso
{
    /**
     * Get an [Observable] that load an image using [Picasso] into a
     * [Target] and emit an event [TargetEvent] for each [Target] events,
     * [Target.onPrepareLoad],
     * [Target.onBitmapLoaded] and
     * [Target.onBitmapFailed].
     *
     * @param tag Any view tied to same lifecycle that this image request. This is used
     * to keep a strong reference to [Target] to make sure it won't get garbage
     * collected before completing this request.
     * @see RequestCreator.into
     */
    @JvmStatic
    fun withTarget(picasso: Picasso,
                   requestCreator: RequestCreator,
                   tag: View): Observable<TargetEvent>
    {
        picasso.cancelTag(tag)

        return Observable
                .create(TargetObservableOnSubscribe(requestCreator, tag))
                .doOnDispose { picasso.cancelTag(tag) }
    }

    /**
     * Get a [Completable] that load an image using [Picasso] into an
     * [ImageView].
     *
     * @see RequestCreator.into
     */
    @JvmStatic
    fun withImageView(picasso: Picasso,
                      requestCreator: RequestCreator,
                      imageView: ImageView): Completable
    {
        picasso.cancelRequest(imageView)

        return Completable
                .create(ImageViewCompletableOnSubscribe(requestCreator, imageView))
                .doOnDispose { picasso.cancelRequest(imageView) }
    }

    class TargetEvent
    {
        @Type
        @get:Type
        val type: String
        val target: Target
        var bitmap: Bitmap? = null
            private set
        var from: Picasso.LoadedFrom? = null
            private set
        var errorDrawable: Drawable? = null
            private set
        var placeHolderDrawable: Drawable? = null
            private set

        internal constructor(target: Target,
                             bitmap: Bitmap,
                             from: Picasso.LoadedFrom)
        {
            this.type = TYPE_BITMAP_LOADED
            this.target = target
            this.bitmap = bitmap
            this.from = from
        }

        internal constructor(@Type type: String,
                             target: Target,
                             drawable: Drawable?)
        {
            if (TYPE_BITMAP_LOADED == type)
                throw IllegalArgumentException(
                        "Type must be TYPE_PREPARE_LOAD or TYPE_BITMAP_FAILED")
            this.type = type
            this.target = target
            if (TYPE_PREPARE_LOAD == type)
                this.placeHolderDrawable = drawable
            else
                this.errorDrawable = drawable
        }

        @Retention(AnnotationRetention.SOURCE)
        @StringDef(TYPE_PREPARE_LOAD, TYPE_BITMAP_LOADED, TYPE_BITMAP_FAILED)
        annotation class Type

        companion object
        {
            const val TYPE_BITMAP_LOADED = "typeBitmapLoaded"
            const val TYPE_BITMAP_FAILED = "typeBitmapFailed"
            const val TYPE_PREPARE_LOAD = "typePrepareLoad"
        }
    }

    private class TargetObservableOnSubscribe constructor(private val requestCreator: RequestCreator,
                                                          private val view: View) : Target,
                                                                                    ObservableOnSubscribe<TargetEvent>
    {
        private lateinit var emitter: ObservableEmitter<TargetEvent>

        override fun subscribe(e: ObservableEmitter<TargetEvent>)
        {
            emitter = e
            view.tag = this
            requestCreator.tag(view)
            requestCreator.into(this)
        }

        override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom)
        {
            emitter.onNext(TargetEvent(this, bitmap, from))
            emitter.onComplete()
        }

        override fun onBitmapFailed(errorDrawable: Drawable)
        {
            emitter.onNext(TargetEvent(TargetEvent.TYPE_BITMAP_FAILED,
                                       this,
                                       errorDrawable))
            if (!emitter.isDisposed)
                emitter.onError(TargetException())
        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable)
        {
            emitter.onNext(TargetEvent(TargetEvent.TYPE_PREPARE_LOAD,
                                       this,
                                       placeHolderDrawable))
        }
    }

    private class ImageViewCompletableOnSubscribe constructor(private val requestCreator: RequestCreator,
                                                              private val imageView: ImageView) :
            CompletableOnSubscribe,
            Callback
    {
        private lateinit var emitter: CompletableEmitter

        @Throws(Exception::class)
        override fun subscribe(e: CompletableEmitter)
        {
            emitter = e
            requestCreator.into(imageView, this)
        }

        override fun onSuccess()
        {
            emitter.onComplete()
        }

        override fun onError()
        {
            if (!emitter.isDisposed)
                emitter.onError(ImageViewException())
        }
    }

    class TargetException : Exception()

    class ImageViewException : Exception()
}
