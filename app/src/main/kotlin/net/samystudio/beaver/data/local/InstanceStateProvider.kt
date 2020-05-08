package net.samystudio.beaver.data.local

import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import java.io.Serializable
import kotlin.reflect.KProperty

abstract class InstanceStateProvider<T>(
    private val bundle: Bundle,
    private var beforeSetCallback: ((value: T) -> T)? = null,
    private var afterSetCallback: ((value: T) -> Unit)? = null
) {
    private var cache: T? = null

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val v = (beforeSetCallback?.let { it(value) } ?: value)

        cache = v
        if (v == null) {
            bundle.remove(property.name)
            return
        }
        when (v) {
            is Byte -> bundle.putByte(property.name, v)
            is ByteArray -> bundle.putByteArray(property.name, v)
            is Short -> bundle.putShort(property.name, v)
            is ShortArray -> bundle.putShortArray(property.name, v)
            is Int -> bundle.putInt(property.name, v)
            is IntArray -> bundle.putIntArray(property.name, v)
            is Long -> bundle.putLong(property.name, v)
            is LongArray -> bundle.putLongArray(property.name, v)
            is Float -> bundle.putFloat(property.name, v)
            is FloatArray -> bundle.putFloatArray(property.name, v)
            is Double -> bundle.putDouble(property.name, v)
            is DoubleArray -> bundle.putDoubleArray(property.name, v)
            is Boolean -> bundle.putBoolean(property.name, v)
            is BooleanArray -> bundle.putBooleanArray(property.name, v)
            is Char -> bundle.putChar(property.name, v)
            is CharArray -> bundle.putCharArray(property.name, v)
            is CharSequence -> bundle.putCharSequence(property.name, v)
            is String -> bundle.putString(property.name, v)
            is Size -> bundle.putSize(property.name, v)
            is SizeF -> bundle.putSizeF(property.name, v)
            is Bundle -> bundle.putBundle(property.name, v)
            is Serializable -> bundle.putSerializable(property.name, v)
            is Parcelable -> bundle.putParcelable(property.name, v)
            is IBinder -> bundle.putBinder(property.name, v)
        }

        afterSetCallback?.let { it(v) }
    }

    @Suppress("UNCHECKED_CAST")
    protected fun getAndCache(key: String): T? =
        cache ?: (bundle.get(key) as T?).apply { cache = this }

    class Nullable<T>(
        savable: Bundle,
        beforeSetCallback: ((value: T) -> T)? = null,
        afterSetCallback: ((value: T) -> Unit)? = null
    ) : InstanceStateProvider<T>(savable, beforeSetCallback, afterSetCallback) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T? =
            getAndCache(property.name)
    }

    class NotNull<T>(
        savable: Bundle,
        private val defaultValue: T,
        beforeSetCallback: ((value: T) -> T)? = null,
        afterSetCallback: ((value: T) -> Unit)? = null
    ) : InstanceStateProvider<T>(savable, beforeSetCallback, afterSetCallback) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
            getAndCache(property.name) ?: defaultValue
    }
}