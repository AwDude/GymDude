package de.itdude.gymdude.repo.db.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Exercise(
    @PrimaryKey var name: String = "",
    var bodyPart: BodyPart? = null,
    var imageUrl: String = ""
): RealmObject()