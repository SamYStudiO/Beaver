package net.samystudio.beaver.ui.base.adapter

import androidx.core.math.MathUtils
import java.util.*

abstract class BaseMutableAdapter<D, VH : BaseViewHolder<D>>(private val _items: MutableList<D> = ArrayList()) :
    BaseAdapter<D, VH>(_items) {
    fun addItem(item: D) = addIemAt(_items.count(), item)

    fun addIemAt(position: Int, item: D) =
        _items.add(position, item).also { notifyItemInserted(position) }

    fun addItems(list: List<D>) = addItemsAt(_items.count(), list)

    fun addItemsAt(position: Int, list: List<D>) =
        _items.addAll(position, list).also { notifyItemRangeInserted(position, list.size) }

    fun removeItem(item: D): Boolean {
        val position = _items.indexOf(item)
        if (position >= 0) {
            _items.remove(item)
            notifyItemRemoved(position)
        }
        return position >= 0
    }

    fun removeItemAt(position: Int) =
        _items.removeAt(position).also { notifyItemRemoved(position) }

    fun removeItems(positionStart: Int, positionEnd: Int) {
        val start = MathUtils.clamp(0, positionStart, _items.size - 1)
        val end = MathUtils.clamp(start, positionEnd, _items.size)
        _items.removeAll(_items.subList(start, end))
        notifyItemRangeRemoved(start, end - start)
    }

    fun setItemAt(position: Int, item: D) =
        _items.set(position, item).also { notifyItemChanged(position) }

    fun clear() {
        _items.clear()
        notifyDataSetChanged()
    }
}