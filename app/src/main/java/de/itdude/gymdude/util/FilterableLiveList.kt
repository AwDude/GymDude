package de.itdude.gymdude.util

/*
@Suppress("MemberVisibilityCanBePrivate")
open class FilterableLiveList<T> private constructor(
    filtered: MutableList<T>,
    protected var unfiltered: List<T>
) : LiveList<T>(filtered) {

    var filter: ((T) -> Boolean) = { true }
    var equals: (T, T) -> Boolean = { item1, item2 -> item1 == item2 }

    constructor(list: List<T> = mutableListOf()) : this(list.toMutableList(), list)

    override fun setValue(value: MutableList<T>?) {
        value ?: return
        unfiltered = value
        filterAll()
    }

    protected fun filterAll() {
        var filteredItemCount = 0

        unfiltered.forEach { item ->
            if (filter(item)) {
                val index = list.indexOfFirst { equals(it, item) }

                if (index < 0) {
                    add(filteredItemCount, item)
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
}*/
