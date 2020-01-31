package de.itdude.gymdude.repo.db

import de.itdude.gymdude.util.LiveDataList
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmResults

class RealmLiveDataList<T : RealmModel>(private val results: RealmResults<T>) : LiveDataList<T>() {

    private val listener = RealmChangeListener<RealmResults<T>> { results -> value = results }

    init {
        if (!results.isValid) {
            throw IllegalArgumentException("The provided RealmResults is no longer valid, the Realm instance it belongs to is closed. It can no longer be observed for changes.")
        }
        if (results.isLoaded) {
            // we should not notify observers when results aren't ready yet (async query).
            // however, synchronous query should be set explicitly.
            value = results
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

}

fun <T : RealmModel> RealmResults<T>.asLiveData() = RealmLiveDataList(this)