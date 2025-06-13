package com.outfitgo.store.core.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

fun convertISODateToReadableDate(isoDate: String): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(isoDate)
        val formatter = DateTimeFormatter.ofPattern("MMM, dd yyyy", Locale.ENGLISH)
        val formattedDate = zonedDateTime.format(formatter)
        return formattedDate
    } catch (_: DateTimeParseException) {
        isoDate
    }
}