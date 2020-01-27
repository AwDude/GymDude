package de.itdude.gymdude.util

import android.content.Context
import android.content.ContextWrapper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


@Suppress("MemberVisibilityCanBePrivate", "unused")
class LiveDataList<T> : LiveData<MutableList<T>?>(), MutableList<T> {

    interface ListObserver {
        fun notifyDataSetChanged()
        fun notifyItemChanged(position: Int)
        fun notifyItemRangeChanged(positionStart: Int, itemCount: Int)
        fun notifyItemInserted(position: Int)
        fun notifyItemRangeInserted(positionStart: Int, itemCount: Int)
        fun notifyItemRemoved(position: Int)
        fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int)
        fun notifyItemMoved(fromPosition: Int, toPosition: Int)
    }

    override val size get() = list.size
    private var list: MutableList<T> = ArrayList()
    private var action: ((ListObserver) -> Unit)? = null
    private val listObservers = ArrayList<ListObserver>()
    private val observer = Observer<MutableList<T>?> { _ ->
        listObservers.forEach { action?.invoke(it) }
    }

    init {
        value = list
    }

    override fun setValue(value: MutableList<T>?) {
        value ?: return
        list = value
        action = { obs -> obs.notifyItemRangeChanged(0, size) }
        super.setValue(value)
    }

    fun notifyObserver() {
        super.setValue(list)
    }

    fun observe(context: Context, listObserver: ListObserver) =
        observe(getLifecycleOwner(context), listObserver)

    fun observe(lifecycleOwner: LifecycleOwner, listObserver: ListObserver) {
        listObservers.add(listObserver)
        if (listObservers.size == 1) {
            super.observe(lifecycleOwner, observer)
        }
    }

    fun stopObserve() {
        super.removeObserver(observer)
        listObservers.clear()
        action = null
    }

    fun stopObserve(listObserver: ListObserver) {
        listObservers.remove(listObserver)
        if (listObservers.isEmpty()) {
            super.removeObserver(observer)
            action = null
        }
    }

    private fun getLifecycleOwner(context: Context): LifecycleOwner {
        var ctx = context
        while (ctx !is LifecycleOwner) {
            ctx = (ctx as ContextWrapper).baseContext
        }
        return ctx
    }

    fun move(fromIndex: Int, toIndex: Int) {
        val item = list.removeAt(fromIndex)
        list.add(toIndex, item)
        action = { obs -> obs.notifyItemMoved(fromIndex, toIndex) }
        notifyObserver()
    }

    override fun add(element: T) = list.add(element).also {
        action = { obs -> obs.notifyItemInserted(size) }
        notifyObserver()
    }

    override fun add(index: Int, element: T) = list.add(index, element).also {
        action = { obs -> obs.notifyItemInserted(index) }
        notifyObserver()
    }

    override fun addAll(index: Int, elements: Collection<T>) = list.addAll(index, elements).also {
        action = { obs -> obs.notifyItemRangeInserted(index, elements.size) }
        notifyObserver()
    }

    override fun addAll(elements: Collection<T>) = list.addAll(elements).also {
        action = { obs -> obs.notifyItemRangeInserted(size, elements.size) }
        notifyObserver()
    }

    override fun clear() = list.clear().also {
        action = { obs -> obs.notifyItemRangeRemoved(0, size) }
        notifyObserver()
    }

    override fun remove(element: T): Boolean {
        val removeIndex = list.indexOf(element)
        val wasRemoved = list.remove(element)
        action = { obs -> obs.notifyItemRemoved(removeIndex) }
        notifyObserver()
        return wasRemoved
    }

    override fun removeAll(elements: Collection<T>) = list.removeAll(elements).also {
        action = { obs -> obs.notifyDataSetChanged() }
        notifyObserver()
    }

    override fun removeAt(index: Int) = list.removeAt(index).also {
        action = { obs -> obs.notifyItemRemoved(index) }
        notifyObserver()
    }

    override fun retainAll(elements: Collection<T>) = list.retainAll(elements).also {
        action = { obs -> obs.notifyDataSetChanged() }
        notifyObserver()
    }

    override fun set(index: Int, element: T) = list.set(index, element).also {
        action = { obs -> obs.notifyItemChanged(index) }
        notifyObserver()
    }

    override fun contains(element: T) = list.contains(element)

    override fun containsAll(elements: Collection<T>) = list.containsAll(elements)

    override fun get(index: Int) = list[index]

    override fun indexOf(element: T) = list.indexOf(element)

    override fun isEmpty() = list.isEmpty()

    override fun iterator() = list.iterator()

    override fun lastIndexOf(element: T) = list.lastIndexOf(element)

    override fun listIterator() = list.listIterator()

    override fun listIterator(index: Int) = list.listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int) = list.subList(fromIndex, toIndex)
}