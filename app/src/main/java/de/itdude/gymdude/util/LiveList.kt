package de.itdude.gymdude.util

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

open class LiveList<T>(private var list: List<T> = emptyList()) :
    MutableLiveData<List<T>>(list), List<T> {

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
    private var action: ((ListObserver) -> Unit)? = null
    private val listObservers = ArrayList<ListObserver>()
    private val observer = Observer<List<T>?> { _ ->
        listObservers.forEach { action?.invoke(it) }
    }

    override fun setValue(value: List<T>) {
        list = value
    }

    protected fun notifyObserver() = super.setValue(list)

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

    fun notifyDataSetChanged() {
        action = { obs -> obs.notifyDataSetChanged() }
        notifyObserver()
    }

    fun notifyItemChanged(position: Int) {
        action = { obs -> obs.notifyItemChanged(position) }
        notifyObserver()
    }

    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) {
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

    protected fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
        action = { obs -> obs.notifyItemRangeRemoved(positionStart, itemCount) }
        notifyObserver()
    }

    protected fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
        action = { obs -> obs.notifyItemMoved(fromPosition, toPosition) }
        notifyObserver()
    }
}