package de.itdude.gymdude.repo.db

import android.util.Log
import de.itdude.gymdude.util.FilterableLiveList
import io.realm.*
import io.realm.kotlin.isValid
import kotlin.reflect.KProperty

@Suppress("MemberVisibilityCanBePrivate", "unused")
class LiveRealmResults<T : RealmModel>(private var result: RealmResults<T>) : FilterableLiveList<T>(result) {

    init {
        equals = { it1, it2 -> it1.isValid() && it1 == it2 }
    }

    private val listener =
        OrderedRealmCollectionChangeListener<RealmResults<T>> { _, changeSet ->
            if (!hasActiveObservers()) {
                return@OrderedRealmCollectionChangeListener
            }
            if (changeSet.state == OrderedCollectionChangeSet.State.ERROR) {
                Log.e("LiveRealmResults", "Realm results are in error state.")
                return@OrderedRealmCollectionChangeListener
            }
            filterAll()
/*            if (!hasListObservers()) {
                notifyObserver()
                return@OrderedRealmCollectionChangeListener
            }
            changeSet.insertionRanges.forEach { range ->
                if (range.length == 1) {
                    notifyItemInserted(range.startIndex)
                } else {
                    notifyItemRangeInserted(range.startIndex, range.length)
                }
            }
            changeSet.changeRanges.forEach { range ->
                if (range.length == 1) {
                    notifyItemChanged(range.startIndex)
                } else {
                    notifyItemRangeChanged(range.startIndex, range.length)
                }
            }
            changeSet.deletionRanges.forEach { range ->
                if (range.length == 1) {
                    notifyItemRemoved(range.startIndex)
                } else {
                    notifyItemRangeRemoved(range.startIndex, range.length)
                }
            }*/
        }

    override fun onActive() = super.onActive().also {
        if (result.isValid) {
            result.addChangeListener(listener)
        }
    }

    override fun onInactive() = super.onInactive().also {
        result.removeChangeListener(listener)
    }

    fun sort(fieldName: String) = updateResult(result.sort(fieldName))

    fun sort(fieldName: String, sortOrder: Sort) = updateResult(result.sort(fieldName, sortOrder))

    fun sort(fieldName1: String, sortOrder1: Sort, fieldName2: String, sortOrder2: Sort) =
        updateResult(result.sort(fieldName1, sortOrder1, fieldName2, sortOrder2))

    fun sort(fieldNames: Array<String?>, sortOrders: Array<Sort?>) =
        updateResult(result.sort(fieldNames, sortOrders))

    fun sort(field: KProperty<*>) = sort(field.name)

    fun sort(field: KProperty<*>, sortOrder: Sort) = sort(field.name, sortOrder)

    fun sort(field1: KProperty<*>, sortOrder1: Sort, field2: KProperty<*>, sortOrder2: Sort)
            = sort(field1.name, sortOrder1, field2.name, sortOrder2)

    fun sort(fields: Array<KProperty<*>>, sortOrders: Array<Sort?>) =
        sort(fields.map { it.name as String? }.toTypedArray(), sortOrders)

    private fun updateResult(newResult: RealmResults<T>) {
        if (!newResult.isValid) {
            return
        }
        newResult.addChangeListener(listener)
        setValue(newResult)
        result.removeChangeListener(listener)
        result = newResult
    }

}