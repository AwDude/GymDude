@file:Suppress("FunctionName", "unused")

package de.itdude.gymdude.util

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import de.itdude.gymdude.repo.db.FilterableLiveRealmCollection
import de.itdude.gymdude.repo.db.LiveRealmCollection
import io.realm.OrderedRealmCollection
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.RealmList
import io.realm.RealmModel
import io.realm.RealmQuery
import io.realm.RealmResults
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.reflect.KProperty

// --- CONTEXT ---

fun Context.getLifecycleOwner(): LifecycleOwner {
	var context = this
	while (context !is LifecycleOwner) {
		context = (context as ContextWrapper).baseContext
	}
	return context
}

// --- FRAGMENT ---

fun <T : Any> Fragment.autoCleared() = AutoClearedValue<T>(this)

// --- DATE TIME ---

fun ZonedDateTime(ms: Long?): ZonedDateTime? = ms?.let { ZonedDateTime(ms) }
fun ZonedDateTime(ms: Long): ZonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault())

fun ZonedDateTime.ms() = this.toInstant().toEpochMilli()
fun ZonedDateTime.daysAgo() = Period.between(this.toLocalDate(), LocalDate.now()).days

// --- ORDERED REALM COLLECTION ---

fun <T : RealmModel> OrderedRealmCollection<T>.asFilterableLive() = FilterableLiveRealmCollection(this)
fun <T : RealmModel> OrderedRealmCollection<T>.asLive() = LiveRealmCollection(this)

@Suppress("UNCHECKED_CAST")
fun <T : RealmModel> OrderedRealmCollection<T>.addListener(listener: OrderedRealmCollectionChangeListener<*>) {
	if (!isManaged || !isValid) return
	when (this) {
		is RealmResults<T> -> addChangeListener(listener as OrderedRealmCollectionChangeListener<RealmResults<T>>)
		is RealmList<T> -> addChangeListener(listener as OrderedRealmCollectionChangeListener<RealmList<T>>)
		else -> throw ClassCastException("A OrderedRealmCollectionChangeListener can only be added to a OrderedRealmCollection of type RealmResults or RealmList!")
	}
}

@Suppress("UNCHECKED_CAST")
fun <T : RealmModel> OrderedRealmCollection<T>.removeListener(listener: OrderedRealmCollectionChangeListener<*>) {
	if (!isManaged || !isValid) return
	when (this) {
		is RealmResults<T> -> removeChangeListener(listener as OrderedRealmCollectionChangeListener<RealmResults<T>>)
		is RealmList<T> -> removeChangeListener(listener as OrderedRealmCollectionChangeListener<RealmList<T>>)
		else -> throw ClassCastException("A OrderedRealmCollectionChangeListener can only be added to a OrderedRealmCollection of type RealmResults or RealmList!")
	}
}

// --- REALM QUERY SORT ---

/*fun <T> RealmQuery<T>.sort(field: KProperty<*>): RealmQuery<T> = sort(field.name)
fun <T> RealmQuery<T>.sort(field: KProperty<*>, sortOrder: Sort): RealmQuery<T> =
    sort(field.name, sortOrder)

fun <T> RealmQuery<T>.sort(
    field1: KProperty<*>, sortOrder1: Sort, field2: KProperty<*>, sortOrder2: Sort
): RealmQuery<T> = sort(field1.name, sortOrder1, field2.name, sortOrder2)

fun <T> RealmQuery<T>.sort(fields: Array<KProperty<*>>, sortOrders: Array<Sort?>): RealmQuery<T> =
    sort(fields.map { it.name as String? }.toTypedArray(), sortOrders)*/

// --- REALM QUERY EQUAL TO ---

fun <T> RealmQuery<T>.equalTo(field: KProperty<String?>, value: String?): RealmQuery<T> = equalTo(field.name, value)

fun <T> RealmQuery<T>.equalTo(field: KProperty<Int?>, value: Int?): RealmQuery<T> = equalTo(field.name, value)

fun <T> RealmQuery<T>.equalTo(field: KProperty<Float?>, value: Float?): RealmQuery<T> = equalTo(field.name, value)

fun <T> RealmQuery<T>.equalTo(field: KProperty<Double?>, value: Double?): RealmQuery<T> = equalTo(field.name, value)

fun <T> RealmQuery<T>.equalTo(field: KProperty<Byte?>, value: Byte?): RealmQuery<T> = equalTo(field.name, value)

fun <T> RealmQuery<T>.equalTo(field: KProperty<Boolean?>, value: Boolean?): RealmQuery<T> = equalTo(field.name, value)

fun <T> RealmQuery<T>.equalTo(field: KProperty<ByteArray?>, value: ByteArray?): RealmQuery<T> = equalTo(field.name, value)

fun <T> RealmQuery<T>.equalTo(field: KProperty<Short?>, value: Short?): RealmQuery<T> = equalTo(field.name, value)

fun <T> RealmQuery<T>.equalTo(field: KProperty<Long?>, value: Long?): RealmQuery<T> = equalTo(field.name, value)

// --- REALM QUERY MAX ---

fun <T> RealmQuery<T>.max(field: KProperty<*>) = max(field.name)

// --- BITMAP ---

fun Bitmap.centerCrop(width: Int, height: Int): Bitmap? {
	val newRatio = width.toFloat() / height
	val oldRatio = this.width.toFloat() / this.height

	return if (newRatio < oldRatio) {
		ThumbnailUtils.extractThumbnail(this, (this.height * newRatio).toInt(), this.height)
	} else {
		ThumbnailUtils.extractThumbnail(this, this.width, (this.width / newRatio).toInt())
	}
}


		/*= if (width >= height) {
	Bitmap.createBitmap(this, width / 2 - height / 2, 0, height, height)
} else {
	Bitmap.createBitmap(this, 0, height / 2 - width / 2, width, width);
}
*/