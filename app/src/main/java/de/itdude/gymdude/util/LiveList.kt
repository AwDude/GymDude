package de.itdude.gymdude.util

import android.content.Context
import android.content.ContextWrapper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

open class LiveList<T>(private var list: List<T> = emptyList()) : MutableLiveData<List<T>>(list), List<T> {

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
	private val observer = Observer<List<T>?> { _ -> listObservers.forEach { action?.invoke(it) } }

	override fun setValue(value: List<T>) {
		list = value
	}

	private fun notifyObserver() = super.setValue(list)

	fun observe(context: Context, listObserver: ListObserver) = observe(context.getLifecycleOwner(), listObserver)

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

	open fun notifyDataSetChanged() {
		action = { obs -> obs.notifyDataSetChanged() }
		notifyObserver()
	}

	open fun notifyItemChanged(position: Int) {
		action = { obs -> obs.notifyItemChanged(position) }
		notifyObserver()
	}

	open fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) {
		action = { obs -> obs.notifyItemRangeChanged(positionStart, itemCount) }
		notifyObserver()
	}

	protected open fun notifyItemInserted(position: Int) {
		action = { obs -> obs.notifyItemInserted(position) }
		notifyObserver()
	}

	protected open fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
		action = { obs -> obs.notifyItemRangeInserted(positionStart, itemCount) }
		notifyObserver()
	}

	protected open fun notifyItemRemoved(position: Int) {
		action = { obs -> obs.notifyItemRemoved(position) }
		notifyObserver()
	}

	protected open fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
		action = { obs -> obs.notifyItemRangeRemoved(positionStart, itemCount) }
		notifyObserver()
	}

	protected open fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
		action = { obs -> obs.notifyItemMoved(fromPosition, toPosition) }
		notifyObserver()
	}
}