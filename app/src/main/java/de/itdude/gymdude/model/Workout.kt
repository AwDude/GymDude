package de.itdude.gymdude.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Index

open class Workout(var name: String = "", @Index var index: Long = -1) : RealmObject() {

	var exercises: RealmList<Exercise> = RealmList()

	override fun toString() = if (isValid) name else "INVALID"

}