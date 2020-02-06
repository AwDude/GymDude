package de.itdude.gymdude.repo.db

import android.util.Log
import de.itdude.gymdude.util.LiveDataList
import io.realm.*

class LiveRealmResults<T : RealmModel>(private var results: RealmResults<T>) :
    LiveDataList<T>(results) {

    private val listener =
        OrderedRealmCollectionChangeListener<RealmResults<T>> { _, changeSet ->
            if (!hasActiveObservers()) {
                return@OrderedRealmCollectionChangeListener
            }
            if (changeSet.state == OrderedCollectionChangeSet.State.ERROR) {
                Log.e("LiveRealmResults","Realm results are in error state.")
                return@OrderedRealmCollectionChangeListener
            }
            if (!hasListObservers()) {
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
            }
        }

    override fun onActive() = super.onActive().also {
        if (results.isValid) {
            results.addChangeListener(listener)
        }
    }

    override fun onInactive() = super.onInactive().also {
        if (results.isValid) {
            results.removeChangeListener(listener)
        }
    }

    fun sort(fieldName: String) {
        val temp = results.sort(fieldName).apply { addChangeListener(listener) }
        value = temp
        if (results.isValid) {
            results.removeChangeListener(listener)
        }
        results = temp
    }

    fun sort(fieldName: String, sortOrder: Sort) {
        val temp = results.sort(fieldName, sortOrder).apply { addChangeListener(listener) }
        value = temp
        if (results.isValid) {
            results.removeChangeListener(listener)
        }
        results = temp
    }

    fun sort(fieldName1: String, sortOrder1: Sort, fieldName2: String, sortOrder2: Sort) {
        val temp = results.sort(fieldName1, sortOrder1, fieldName2, sortOrder2).apply {
            addChangeListener(listener)
        }
        value = temp
        if (results.isValid) {
            results.removeChangeListener(listener)
        }
        results = temp
    }

    fun sort(fieldNames: Array<String?>, sortOrders: Array<Sort?>) {
        val temp = results.sort(fieldNames, sortOrders).apply { addChangeListener(listener) }
        value = temp
        if (results.isValid) {
            results.removeChangeListener(listener)
        }
        results = temp
    }

}