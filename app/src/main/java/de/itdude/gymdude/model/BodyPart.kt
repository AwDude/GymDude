package de.itdude.gymdude.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class BodyPart(
    @PrimaryKey var name: String = "",
    var imageUrl: String = ""
) : RealmObject() {

    override fun toString() = if (isValid) name else "INVALID"
}