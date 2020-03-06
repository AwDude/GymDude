package de.itdude.gymdude.model

import de.itdude.gymdude.util.ZonedDateTime
import de.itdude.gymdude.util.ms
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.time.ZonedDateTime


open class Exercise(
    @PrimaryKey var name: String = "",
    var bodyPart: BodyPart? = null,
    var imageUrl: String = ""
) : RealmObject() {

    var lastTimeDoneMs: Long? = null
    var lastTimeDone: ZonedDateTime?
        get() = ZonedDateTime(lastTimeDoneMs)
        set(value) {
            lastTimeDoneMs = value?.ms()
        }

    override fun toString() = if (isValid) name else "INVALID"
}