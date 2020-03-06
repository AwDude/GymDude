package de.itdude.gymdude.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class WorkoutPlan(
    @PrimaryKey var name: String = ""
) : RealmObject() {
    var workouts: RealmList<Workout> = RealmList()

    override fun toString() = if (isValid) name else "INVALID"
}