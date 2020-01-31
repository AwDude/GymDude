package de.itdude.gymdude.repo.db.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class BodyPart(
    @PrimaryKey var name: String = "",
    var imageUrl: String = ""
) : RealmObject()