package de.itdude.gymdude.repo.db

import android.util.Log
import de.itdude.gymdude.util.LiveList
import de.itdude.gymdude.util.addListener
import de.itdude.gymdude.util.removeListener
import io.realm.*
import kotlin.reflect.KProperty

@Suppress("MemberVisibilityCanBePrivate", "unused")
class LiveRealmCollection<T : RealmModel>(private var realmCollection: OrderedRealmCollection<T>) :
    LiveList<T>(realmCollection) {

    private var savedSize = size

    override val size: Int
        get() = if (realmCollection.isValid) {
            savedSize = super.size
            super.size
        } else {
            savedSize
        }

    private val listener =
        OrderedRealmCollectionChangeListener<OrderedRealmCollection<T>> { _, changeSet ->
            if (!hasActiveObservers()) {
                return@OrderedRealmCollectionChangeListener
            }
            if (changeSet.state == OrderedCollectionChangeSet.State.ERROR) {
                Log.e("LiveRealmResults", "Realm results are in error state.")
                return@OrderedRealmCollectionChangeListener
            }
            if (!hasListObservers()) {
                notifyDataSetChanged()
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

    override fun onActive() = super.onActive().also { realmCollection.addListener(listener) }

    override fun onInactive() = super.onInactive().also { realmCollection.removeListener(listener) }

    fun sort(fieldName: String) = setValue(realmCollection.sort(fieldName))

    fun sort(fieldName: String, sortOrder: Sort) =
        setValue(realmCollection.sort(fieldName, sortOrder))

    fun sort(fieldName1: String, sortOrder1: Sort, fieldName2: String, sortOrder2: Sort) =
        setValue(realmCollection.sort(fieldName1, sortOrder1, fieldName2, sortOrder2))

    fun sort(fieldNames: Array<String?>, sortOrders: Array<Sort?>) =
        setValue(realmCollection.sort(fieldNames, sortOrders))

    fun sort(field: KProperty<*>) = sort(field.name)

    fun sort(field: KProperty<*>, sortOrder: Sort) = sort(field.name, sortOrder)

    fun sort(field1: KProperty<*>, sortOrder1: Sort, field2: KProperty<*>, sortOrder2: Sort) =
        sort(field1.name, sortOrder1, field2.name, sortOrder2)

    fun sort(fields: Array<KProperty<*>>, sortOrders: Array<Sort?>) =
        sort(fields.map { it.name as String? }.toTypedArray(), sortOrders)

    fun setValue(newRealmCollection: OrderedRealmCollection<T>) {
        newRealmCollection.addListener(listener)
        super.setValue(newRealmCollection)
        realmCollection.removeListener(listener)
        realmCollection = newRealmCollection
        notifyDataSetChanged()
    }

}