@file:Suppress("FunctionName", "unused")

package de.itdude.gymdude.util

import java.time.*

fun ZonedDateTime(ms: Long?): ZonedDateTime? = ms?.let { ZonedDateTime(ms) }
fun ZonedDateTime(ms: Long): ZonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault())
fun ZonedDateTime.ms() = this.toInstant().toEpochMilli()
fun ZonedDateTime.daysAgo() = Period.between(this.toLocalDate(), LocalDate.now()).days