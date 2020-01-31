package de.itdude.gymdude.repo.db

import android.util.Log
import de.itdude.gymdude.util.LiveDataList
import io.realm.OrderedCollectionChangeSet
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.RealmModel
import io.realm.RealmResults

class RealmLiveDataList<T : RealmModel>(private val results: RealmResults<T>) : LiveDataList<T>() {

    private val listener =
        OrderedRealmCollectionChangeListener<RealmResults<T>> { _, changeSet ->
            if (!hasActiveObservers()) {
                return@OrderedRealmCollectionChangeListener
            }
            if (changeSet.state == OrderedCollectionChangeSet.State.ERROR) {
                Log.e("RealmLiveDataList",
                    "OrderedRealmCollectionChangeListener returned an error.")
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

    init {
        if (!results.isValid) {
            throw IllegalArgumentException("The provided RealmResults is no longer valid, the Realm instance it belongs to is closed. It can no longer be observed for changes.")
        }
        value = results
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

}

fun <T : RealmModel> RealmResults<T>.asLiveData() = RealmLiveDataList(this)