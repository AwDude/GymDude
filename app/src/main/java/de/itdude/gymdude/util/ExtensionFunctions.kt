@file:Suppress("FunctionName", "unused")

package de.itdude.gymdude.util

import androidx.fragment.app.Fragment
import de.itdude.gymdude.repo.db.FilterableLiveRealmResults
import de.itdude.gymdude.repo.db.LiveRealmResults
import io.realm.RealmModel
import io.realm.RealmQuery
import io.realm.RealmResults
import java.time.*
import kotlin.reflect.KProperty

// --- FRAGMENT ---

fun <T : Any> Fragment.autoCleared() = AutoClearedValue<T>(this)

// --- DATE TIME ---

fun ZonedDateTime(ms: Long?): ZonedDateTime? = ms?.let { ZonedDateTime(ms) }
fun ZonedDateTime(ms: Long): ZonedDateTime =
    ZonedDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault())

fun ZonedDateTime.ms() = this.toInstant().toEpochMilli()
fun ZonedDateTime.daysAgo() = Period.between(this.toLocalDate(), LocalDate.now()).days

// --- LIVE REALM RESULTS ---

fun <T : RealmModel> RealmResults<T>.asFilterableLive() = FilterableLiveRealmResults(this)
fun <T : RealmModel> RealmResults<T>.asLive() = LiveRealmResults(this)

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

fun <T> RealmQuery<T>.equalTo(field: KProperty<String?>, value: String?): RealmQuery<T> =
    equalTo(field.name, value)

fun <T> RealmQuery<T>.equalTo(field: KProperty<Int?>, value: Int?): RealmQuery<T> =
    equalTo(field.name, value)

fun <T> RealmQuery<T>.equalTo(field: KProperty<Float?>, value: Float?): RealmQuery<T> =
    equalTo(field.name, value)

fun <T> RealmQuery<T>.equalTo(field: KProperty<Double?>, value: Double?): RealmQuery<T> =
    equalTo(field.name, value)

fun <T> RealmQuery<T>.equalTo(field: KProperty<Byte?>, value: Byte?): RealmQuery<T> =
    equalTo(field.name, value)

fun <T> RealmQuery<T>.equalTo(field: KProperty<Boolean?>, value: Boolean?): RealmQuery<T> =
    equalTo(field.name, value)

fun <T> RealmQuery<T>.equalTo(field: KProperty<ByteArray?>, value: ByteArray?): RealmQuery<T> =
    equalTo(field.name, value)

fun <T> RealmQuery<T>.equalTo(field: KProperty<Short?>, value: Short?): RealmQuery<T> =
    equalTo(field.name, value)

fun <T> RealmQuery<T>.equalTo(field: KProperty<Long?>, value: Long?): RealmQuery<T> =
    equalTo(field.name, value)