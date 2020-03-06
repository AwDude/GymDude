package de.itdude.gymdude.model

import io.realm.RealmList
import io.realm.RealmObject

open class Workout(
    var name: String = ""
) : RealmObject() {
    var exercises: RealmList<Exercise> = RealmList()

    override fun toString() = if (isValid) name else "INVALID"
}