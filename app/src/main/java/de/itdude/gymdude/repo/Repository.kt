package de.itdude.gymdude.repo

import io.realm.Realm
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val db: Realm) {

}