package de.itdude.gymdude.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Workout(
    var name: String = ""
) : RealmObject() {
    var exercises: RealmList<Exercise> = RealmList()
}