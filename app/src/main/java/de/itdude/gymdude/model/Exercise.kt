package de.itdude.gymdude.model

import de.itdude.gymdude.util.ZonedDateTime
import de.itdude.gymdude.util.ms
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.time.ZonedDateTime


open class Exercise(
    @PrimaryKey var name: String = "",
    var bodyPart: BodyPart? = null,
    var imageUrl: String = "",
    private var lastTimeDone: Long? = null
) : RealmObject() {

    fun getLastTimeDone() = ZonedDateTime(lastTimeDone)
    fun setLastTimeDone(time: ZonedDateTime) { lastTimeDone = time.ms() }

}