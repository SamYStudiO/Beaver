package net.samystudio.beaver.ui.base.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class BaseAdapterTest {

    private lateinit var adapter: BaseMutableAdapter<Int, BaseViewHolder<Int>>

    @Before
    fun setUp() {
        val view = mock(View::class.java)
        val list = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        adapter = object : BaseMutableAdapter<Int, BaseViewHolder<Int>>(list.toMutableList()) {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): BaseViewHolder<Int> {
                return object : BaseViewHolder<Int>(view) {
                    override fun onDataChanged() {

                    }
                }
            }
        }
    }

    @Test
    fun addItem() {
        val value = 10
        var listenerOk = false
        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                assertEquals(positionStart, 10)
                assertEquals(itemCount, 1)
                listenerOk = true
            }
        }
        adapter.registerAdapterDataObserver(observer)
        adapter.addItem(value)

        assertEquals(adapter.getItemAt(10), value)
        assertTrue(listenerOk)
        adapter.unregisterAdapterDataObserver(observer)
    }

    @Test
    fun addIemAt() {
        val position = 5
        val value = 10
        var listenerOk = false
        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                assertEquals(positionStart, position)
                assertEquals(itemCount, 1)
                listenerOk = true
            }
        }
        adapter.registerAdapterDataObserver(observer)
        adapter.addIemAt(position, value)

        assertEquals(adapter.getItemAt(position), value)
        assertTrue(listenerOk)
        adapter.unregisterAdapterDataObserver(observer)
    }

    @Test
    fun addItems() {
        var listenerOk = false
        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                assertEquals(positionStart, 10)
                assertEquals(itemCount, 3)
                listenerOk = true
            }
        }
        adapter.registerAdapterDataObserver(observer)
        adapter.addItems(listOf(10, 11, 12))

        assertEquals(adapter.getItemAt(10), 10)
        assertEquals(adapter.getItemAt(11), 11)
        assertEquals(adapter.getItemAt(12), 12)
        assertTrue(listenerOk)
        adapter.unregisterAdapterDataObserver(observer)
    }

    @Test
    fun addItemsAt() {
        val position = 5
        var listenerOk = false
        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                assertEquals(positionStart, position)
                assertEquals(itemCount, 3)
                listenerOk = true
            }
        }
        adapter.registerAdapterDataObserver(observer)
        adapter.addItemsAt(position, listOf(10, 11, 12))

        assertEquals(adapter.getItemAt(5), 10)
        assertEquals(adapter.getItemAt(6), 11)
        assertEquals(adapter.getItemAt(7), 12)
        assertTrue(listenerOk)
        adapter.unregisterAdapterDataObserver(observer)
    }

    @Test
    fun removeItem() {
        var listenerOk = false
        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                assertEquals(positionStart, 5)
                assertEquals(itemCount, 1)
                listenerOk = true
            }
        }
        adapter.registerAdapterDataObserver(observer)
        adapter.removeItem(5)

        assertEquals(adapter.getItemAt(5), 6)
        assertEquals(adapter.itemCount, 9)
        assertTrue(listenerOk)
        adapter.unregisterAdapterDataObserver(observer)
    }

    @Test
    fun removeItemAt() {
        val position = 3
        var listenerOk = false
        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                assertEquals(positionStart, position)
                assertEquals(itemCount, 1)
                listenerOk = true
            }
        }
        adapter.registerAdapterDataObserver(observer)
        adapter.removeItemAt(position)

        assertEquals(adapter.getItemAt(3), 4)
        assertEquals(adapter.itemCount, 9)
        assertTrue(listenerOk)
        adapter.unregisterAdapterDataObserver(observer)
    }

    @Test
    fun removeItems() {
        var listenerOk = false
        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                assertEquals(positionStart, 4)
                assertEquals(itemCount, 2)
                listenerOk = true
            }
        }
        adapter.registerAdapterDataObserver(observer)
        adapter.removeItems(4, 6)

        assertEquals(adapter.getItemAt(4), 6)
        assertEquals(adapter.getItemAt(5), 7)
        assertEquals(adapter.itemCount, 8)
        assertTrue(listenerOk)
        adapter.unregisterAdapterDataObserver(observer)
    }

    @Test
    fun setItemAt() {
        val position = 4
        val value = 44
        var listenerOk = false
        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                assertEquals(positionStart, position)
                assertEquals(itemCount, 1)
                listenerOk = true
            }
        }
        adapter.registerAdapterDataObserver(observer)
        adapter.setItemAt(position, value)

        assertEquals(adapter.getItemAt(position), value)
        assertTrue(listenerOk)
        adapter.unregisterAdapterDataObserver(observer)
    }

    @Test
    fun clearData() {
        var listenerOk = false
        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                listenerOk = true
            }
        }
        adapter.registerAdapterDataObserver(observer)
        adapter.clear()

        assertEquals(adapter.itemCount, 0)
        assertTrue(listenerOk)
        adapter.unregisterAdapterDataObserver(observer)
    }
}