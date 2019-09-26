package net.samystudio.beaver.data.local

import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import java.io.Serializable
import kotlin.reflect.KProperty

abstract class InstanceStateProvider<T>(private val bundle: Bundle) {

    private var cache: T? = null

    open operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        cache = value
        if (value == null) {
            bundle.remove(property.name)
            return
        }
        when (value) {
            is Byte -> bundle.putByte(property.name, value)
            is ByteArray -> bundle.putByteArray(property.name, value)
            is Short -> bundle.putShort(property.name, value)
            is ShortArray -> bundle.putShortArray(property.name, value)
            is Int -> bundle.putInt(property.name, value)
            is IntArray -> bundle.putIntArray(property.name, value)
            is Long -> bundle.putLong(property.name, value)
            is LongArray -> bundle.putLongArray(property.name, value)
            is Float -> bundle.putFloat(property.name, value)
            is FloatArray -> bundle.putFloatArray(property.name, value)
            is Double -> bundle.putDouble(property.name, value)
            is DoubleArray -> bundle.putDoubleArray(property.name, value)
            is Boolean -> bundle.putBoolean(property.name, value)
            is BooleanArray -> bundle.putBooleanArray(property.name, value)
            is Char -> bundle.putChar(property.name, value)
            is CharArray -> bundle.putCharArray(property.name, value)
            is CharSequence -> bundle.putCharSequence(property.name, value)
            is String -> bundle.putString(property.name, value)
            is Size -> bundle.putSize(property.name, value)
            is SizeF -> bundle.putSizeF(property.name, value)
            is Bundle -> bundle.putBundle(property.name, value)
            is Serializable -> bundle.putSerializable(property.name, value)
            is Parcelable -> bundle.putParcelable(property.name, value)
            is IBinder -> bundle.putBinder(property.name, value)
        }
    }

    @Suppress("UNCHECKED_CAST")
    protected fun getAndCache(key: String): T? =
        cache ?: (bundle.get(key) as T?).apply { cache = this }

    class Nullable<T>(savable: Bundle, private var setterCallback: ((value: T) -> Unit)? = null) :
        InstanceStateProvider<T>(savable) {

        override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            super.setValue(thisRef, property, value)
            setterCallback?.let { it(value) }
            // setterCallback = null
        }

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T? =
            getAndCache(property.name)
    }

    class NotNull<T>(
        savable: Bundle,
        private val defaultValue: T,
        private var setterCallback: ((value: T) -> Unit)? = null
    ) :
        InstanceStateProvider<T>(savable) {
        override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            super.setValue(thisRef, property, value)
            setterCallback?.let { it(value) }
            // setterCallback = null
        }

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
            getAndCache(property.name) ?: defaultValue
    }
}