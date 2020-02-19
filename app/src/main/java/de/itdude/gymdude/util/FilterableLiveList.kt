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

}