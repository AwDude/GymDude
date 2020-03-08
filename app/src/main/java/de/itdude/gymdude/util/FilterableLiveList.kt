package de.itdude.gymdude.util

@Suppress("MemberVisibilityCanBePrivate", "unused")
open class FilterableLiveList<T> private constructor(private var filtered: MutableList<T>, private var unfiltered: List<T>) :
	LiveList<T>(filtered) {

	protected var equals: (T, T) -> Boolean = { item1, item2 -> item1 == item2 }
	private var filterPredicate: ((T) -> Boolean) = { true }
	private var indices = filtered.indices.toMutableList()

	constructor(list: List<T> = emptyList()) : this(list.toMutableList(), list)

	fun setFilter(predicate: (T) -> Boolean) {
		filterPredicate = predicate
	}

	fun filter(predicate: (T) -> Boolean) {
		filterPredicate = predicate
		filter()
	}

	fun filter() {
		var filteredItemCount = 0
		var indexModifier = 0

		unfiltered.forEachIndexed { i, item ->
			val index = indices[i] + indexModifier

			if (filterPredicate(item)) {
				if (indices[i] == -1) {
					filtered.add(filteredItemCount, item)
					super.notifyItemInserted(filteredItemCount)
					indexModifier++
				} else {
					removeRange(filteredItemCount, index)
				}
				indices[i] = filteredItemCount
				filteredItemCount++
			} else if (indices[i] != -1) {
				filtered.removeAt(index)
				super.notifyItemRemoved(index)
				indices[i] = -1
				indexModifier--
			}
		}
	}

	private fun filterNewList() {
		indices.clear()
		var filteredItemCount = 0

		unfiltered.forEach { item ->
			if (filterPredicate(item)) {
				val index = filtered.indexOfFirst { equals(it, item) }

				if (index < 0) {
					filtered.add(filteredItemCount, item)
					super.notifyItemInserted(filteredItemCount)
				} else if (index != filteredItemCount) {
					filtered.removeAt(index)
					filtered.add(filteredItemCount, item)
					super.notifyItemMoved(index, filteredItemCount)
				}

				indices.add(filteredItemCount)
				filteredItemCount++
			} else {
				indices.add(-1)
			}
		}
		removeRange(filteredItemCount, filtered.size)
	}

	private fun removeRange(fromIndex: Int, toIndex: Int) {
		if (fromIndex >= toIndex) return
		filtered.subList(fromIndex, toIndex).clear()
		super.notifyItemRangeRemoved(fromIndex, toIndex - fromIndex)
	}

	private fun findIndex(position: Int): Int {
		var newIndex = 0
		for (i in position - 1 downTo 0) {
			if (indices[i] != -1) {
				newIndex = indices[i] + 1
				break
			}
		}
		return newIndex
	}

	private fun addFilteredItem(position: Int, item: T) {
		val newIndex = findIndex(position)
		filtered.add(newIndex, item)
		super.notifyItemInserted(newIndex)
		indices[position] = newIndex

		for (i in position + 1 until indices.size) {
			if (indices[i] != -1) {
				indices[i]++
			}
		}
	}

	private fun removeFilteredItem(position: Int, index: Int) {
		filtered.removeAt(index)
		super.notifyItemRemoved(index)
		indices[position] = -1

		for (i in position + 1 until indices.size) {
			if (indices[i] != -1) {
				indices[i]--
			}
		}
	}

	override fun setValue(value: List<T>) {
		unfiltered = value
		filterNewList()
	}

	override fun notifyDataSetChanged() = filterNewList()

	override fun notifyItemChanged(position: Int) {
		val item = unfiltered[position]
		val index = indices[position]

		if (filterPredicate(item)) {
			if (index == -1) {
				addFilteredItem(position, item)
			} else {
				super.notifyItemChanged(index)
			}
		} else if (index != -1) {
			removeFilteredItem(position, index)
		}
	}

	override fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) {
		for (i in positionStart until positionStart + itemCount) {
			notifyItemChanged(i)
		}
	}

	override fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
		val index = indices.removeAt(fromPosition)
		indices.add(toPosition, index)

		if (index != -1) {
			if (fromPosition < toPosition) {
				for (i in fromPosition until toPosition) {
					if (indices[i] != -1) {
						indices[i]--
						indices[toPosition]++
					}
				}
			} else if (toPosition < fromPosition) {
				for (i in toPosition + 1..fromPosition) {
					if (indices[i] != -1) {
						indices[i]++
						indices[toPosition]--
					}
				}
			}
			val item = filtered.removeAt(index)
			filtered.add(indices[toPosition], item)
			super.notifyItemMoved(index, indices[toPosition])
		}
	}

	override fun notifyItemInserted(position: Int) {
		indices.add(position, -1)
		val item = unfiltered[position]

		if (filterPredicate(item)) {
			addFilteredItem(position, item)
		}
	}

	override fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
		var toInsertCount = 0
		val indicesToInsert = ArrayList<Int>()
		val indexStart = findIndex(positionStart)

		for (i in positionStart until positionStart + itemCount) {
			val item = unfiltered[i]
			if (filterPredicate(item)) {
				indicesToInsert.add(indexStart + toInsertCount)
				filtered.add(indexStart + toInsertCount, item)
				toInsertCount++
			} else {
				indicesToInsert.add(-1)
			}
		}
		indices.addAll(positionStart, indicesToInsert)

		if (toInsertCount > 0) {
			super.notifyItemRangeInserted(indexStart, toInsertCount)

			for (i in positionStart + itemCount until indices.size) {
				if (indices[i] != -1) {
					indices[i] += toInsertCount
				}
			}
		}
	}

	override fun notifyItemRemoved(position: Int) {
		val index = indices.removeAt(position)

		if (index != -1) {
			filtered.removeAt(index)
			super.notifyItemRemoved(index)

			for (i in position until indices.size) {
				if (indices[i] != -1) {
					indices[i]--
				}
			}
		}
	}

	override fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
		var toRemoveCount = 0
		var indexStart = 0

		for (i in positionStart + itemCount - 1 downTo positionStart) {
			if (indices[i] != -1) {
				toRemoveCount++
				indexStart = indices[i]
			}
		}

		removeRange(indexStart, indexStart + toRemoveCount)
		indices.subList(positionStart, positionStart + itemCount).clear()

		for (i in positionStart until indices.size) {
			if (indices[i] != -1) {
				indices[i] -= toRemoveCount
			}
		}
	}

}