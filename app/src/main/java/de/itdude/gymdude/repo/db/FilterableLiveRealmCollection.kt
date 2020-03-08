package de.itdude.gymdude.repo.db

import android.util.Log
import de.itdude.gymdude.util.FilterableLiveList
import de.itdude.gymdude.util.addListener
import de.itdude.gymdude.util.removeListener
import io.realm.OrderedCollectionChangeSet
import io.realm.OrderedRealmCollection
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.RealmList
import io.realm.RealmModel
import io.realm.Sort
import io.realm.kotlin.isValid
import kotlin.reflect.KProperty

@Suppress("MemberVisibilityCanBePrivate", "unused")
class FilterableLiveRealmCollection<T : RealmModel>(private var realmCollection: OrderedRealmCollection<T> = RealmList()) :
	FilterableLiveList<T>(realmCollection) {

	private var savedSize = size

	override val size: Int
		get() {
			if (realmCollection.isValid) savedSize = super.size
			return savedSize
		}

	private val listener = OrderedRealmCollectionChangeListener<OrderedRealmCollection<T>> { _, changeSet ->
		if (!hasActiveObservers()) {
			return@OrderedRealmCollectionChangeListener
		}
		if (changeSet.state == OrderedCollectionChangeSet.State.ERROR) {
			Log.e("LiveRealmResults", "Realm results are in error state.")
			return@OrderedRealmCollectionChangeListener
		}
		// workaround to recognize if item was moved
		if (changeSet.insertions.size == 1 && changeSet.deletions.size == 1) {
			notifyItemMoved(changeSet.deletions[0], changeSet.insertions[0])
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
		equals = { it1, it2 -> it1.isValid() && it1 == it2 }
	}

	override fun onActive() = super.onActive().also { realmCollection.addListener(listener) }

	override fun onInactive() = super.onInactive().also { realmCollection.removeListener(listener) }

	fun clear() = setValue(RealmList())

	fun sort(fieldName: String) = setValue(realmCollection.sort(fieldName))

	fun sort(fieldName: String, sortOrder: Sort) = setValue(realmCollection.sort(fieldName, sortOrder))

	fun sort(fieldName1: String, sortOrder1: Sort, fieldName2: String, sortOrder2: Sort) =
		setValue(realmCollection.sort(fieldName1, sortOrder1, fieldName2, sortOrder2))

	fun sort(fieldNames: Array<String?>, sortOrders: Array<Sort?>) = setValue(realmCollection.sort(fieldNames, sortOrders))

	fun sort(field: KProperty<*>) = sort(field.name)

	fun sort(field: KProperty<*>, sortOrder: Sort) = sort(field.name, sortOrder)

	fun sort(field1: KProperty<*>, sortOrder1: Sort, field2: KProperty<*>, sortOrder2: Sort) =
		sort(field1.name, sortOrder1, field2.name, sortOrder2)

	@Suppress("USELESS_CAST")
	fun sort(fields: Array<KProperty<*>>, sortOrders: Array<Sort?>) = sort(fields.map { it.name as String? }.toTypedArray(), sortOrders)

	fun setValue(newRealmCollection: OrderedRealmCollection<T>) {
		newRealmCollection.addListener(listener)
		super.setValue(newRealmCollection)
		realmCollection.removeListener(listener)
		realmCollection = newRealmCollection
	}

}