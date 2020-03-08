package de.itdude.gymdude.util

@Suppress("unused")
open class MutableLiveList<T>(private var list: MutableList<T> = mutableListOf()) : LiveList<T>(list), MutableList<T> {

	@JvmName("setMutableValue")
	fun setValue(value: MutableList<T>) {
		list = value
		notifyDataSetChanged()
	}

	fun move(fromIndex: Int, toIndex: Int) {
		val item = list.removeAt(fromIndex)
		list.add(toIndex, item)
		notifyItemMoved(fromIndex, toIndex)
	}

	override fun add(element: T) = list.add(element).also { notifyItemInserted(size) }

	override fun add(index: Int, element: T) = list.add(index, element).also { notifyItemInserted(index) }

	override fun addAll(index: Int, elements: Collection<T>) = list.addAll(index, elements).also {
		notifyItemRangeInserted(index, elements.size)
	}

	override fun addAll(elements: Collection<T>) = list.addAll(elements).also {	notifyItemRangeInserted(size, elements.size) }

	override fun clear() {
		val itemCount = size
		list.clear()
		notifyItemRangeRemoved(0, itemCount)
	}

	override fun remove(element: T): Boolean {
		val removeIndex = list.indexOf(element)
		val wasRemoved = list.remove(element)
		notifyItemRemoved(removeIndex)
		return wasRemoved
	}

	override fun removeAll(elements: Collection<T>) = list.removeAll(elements).also { notifyDataSetChanged() }

	fun removeRange(fromIndex: Int, toIndex: Int) = list.subList(fromIndex, toIndex).clear().also {
		notifyItemRangeRemoved(fromIndex, toIndex - fromIndex)
	}

	override fun removeAt(index: Int) = list.removeAt(index).also {	notifyItemRemoved(index) }

	override fun retainAll(elements: Collection<T>) = list.retainAll(elements).also { notifyDataSetChanged() }

	override fun set(index: Int, element: T) = list.set(index, element).also { notifyItemChanged(index)	}

	override fun iterator() = list.listIterator()

	override fun listIterator() = list.listIterator()

	override fun listIterator(index: Int) = list.listIterator(index)

	override fun subList(fromIndex: Int, toIndex: Int) = list.subList(fromIndex, toIndex)
}