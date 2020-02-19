package de.itdude.gymdude.util

open class FilterableLiveList<T> private constructor(
    protected var filtered: MutableList<T>, protected var unfiltered: List<T>
) : LiveList<T>(filtered) {

    var filter: ((T) -> Boolean) = { true }
    var equals: (T, T) -> Boolean = { item1, item2 -> item1 == item2 }

    constructor(list: List<T> = emptyList()) : this(list.toMutableList(), list)

    override fun setValue(value: List<T>) {
        unfiltered = value
        filterAll()
    }

    fun filterAll() {
        var filteredItemCount = 0

        unfiltered.forEach { item ->
            if (filter(item)) {
                val index = filtered.indexOfFirst { equals(it, item) }

                if (index < 0) {
                    filtered.add(filteredItemCount, item)
                    super.notifyItemInserted(filteredItemCount)
                } else {
                    removeRange(filteredItemCount, index)
                }

                filteredItemCount++
            }
        }

        if (filteredItemCount < size) {
            removeRange(filteredItemCount, size)
        }
    }

    private fun removeRange(fromIndex: Int, toIndex: Int) {
        filtered.subList(fromIndex, toIndex).clear()
        super.notifyItemRangeRemoved(fromIndex, toIndex - fromIndex)
    }

    override fun notifyDataSetChanged() {
        filterAll()
    }

/*    override fun notifyItemChanged(position: Int) {
        action = { obs -> obs.notifyItemChanged(position) }
        notifyObserver()
    }

    override fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) {
        action = { obs -> obs.notifyItemRangeChanged(positionStart, itemCount) }
        notifyObserver()
    }

    override fun notifyItemInserted(position: Int) {
        action = { obs -> obs.notifyItemInserted(position) }
        notifyObserver()
    }

    override fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
        action = { obs -> obs.notifyItemRangeInserted(positionStart, itemCount) }
        notifyObserver()
    }

    override fun notifyItemRemoved(position: Int) {
        action = { obs -> obs.notifyItemRemoved(position) }
        notifyObserver()
    }

    override fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
        action = { obs -> obs.notifyItemRangeRemoved(positionStart, itemCount) }
        notifyObserver()
    }

    override fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
        action = { obs -> obs.notifyItemMoved(fromPosition, toPosition) }
        notifyObserver()
    }*/
}