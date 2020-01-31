package de.itdude.gymdude.util

import android.content.Context
import android.content.ContextWrapper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


@Suppress("MemberVisibilityCanBePrivate", "unused")
open class LiveDataList<T> : LiveData<MutableList<T>?>(), MutableList<T> {

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

    @Synchronized
    fun observe(lifecycleOwner: LifecycleOwner, listObserver: ListObserver) {
        listObservers.add(listObserver)
        if (listObservers.size == 1) {
            super.observe(lifecycleOwner, observer)
        }
    }

    @Synchronized
    fun stopObserve() {
        super.removeObserver(observer)
        listObservers.clear()
        action = null
    }

    @Synchronized
    fun stopObserve(listObserver: ListObserver) {
        listObservers.remove(listObserver)
        if (listObservers.isEmpty()) {
            super.removeObserver(observer)
            action = null
        }
    }

    @Synchronized
    fun hasListObservers() = listObservers.isNotEmpty()

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
        notifyItemMoved(fromIndex, toIndex)
    }

    override fun add(element: T) = list.add(element).also {
        notifyItemInserted(size)
    }

    override fun add(index: Int, element: T) = list.add(index, element).also {
        notifyItemInserted(index)
    }

    override fun addAll(index: Int, elements: Collection<T>) = list.addAll(index, elements).also {
        notifyItemRangeInserted(index, elements.size)
    }

    override fun addAll(elements: Collection<T>) = list.addAll(elements).also {
        notifyItemRangeInserted(size, elements.size)
    }

    override fun clear() = list.clear().also {
        notifyItemRangeRemoved(0, size)
    }

    override fun remove(element: T): Boolean {
        val removeIndex = list.indexOf(element)
        val wasRemoved = list.remove(element)
        notifyItemRemoved(removeIndex)
        return wasRemoved
    }

    override fun removeAll(elements: Collection<T>) = list.removeAll(elements).also {
        notifyDataSetChanged()
    }

    override fun removeAt(index: Int) = list.removeAt(index).also {
        notifyItemRemoved(index)
    }

    override fun retainAll(elements: Collection<T>) = list.retainAll(elements).also {
        notifyDataSetChanged()
    }

    override fun set(index: Int, element: T) = list.set(index, element).also {
        notifyItemChanged(index)
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

    protected fun notifyDataSetChanged() {
        action = { obs -> obs.notifyDataSetChanged() }
        notifyObserver()
    }

    protected fun notifyItemChanged(position: Int) {
        action = { obs -> obs.notifyItemChanged(position) }
        notifyObserver()
    }

    protected fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) {
        action = { obs -> obs.notifyItemRangeChanged(positionStart, itemCount) }
        notifyObserver()
    }

    protected fun notifyItemInserted(position: Int) {
        action = { obs -> obs.notifyItemInserted(position) }
        notifyObserver()
    }

    protected fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
        action = { obs -> obs.notifyItemRangeInserted(positionStart, itemCount) }
        notifyObserver()
    }

    protected fun notifyItemRemoved(position: Int) {
        action = { obs -> obs.notifyItemRemoved(position) }
        notifyObserver()
    }

    @Suppress("SameParameterValue")
    protected fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
        action = { obs -> obs.notifyItemRangeRemoved(positionStart, itemCount) }
        notifyObserver()
    }

    protected fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
        action = { obs -> obs.notifyItemMoved(fromPosition, toPosition) }
        notifyObserver()
    }
}