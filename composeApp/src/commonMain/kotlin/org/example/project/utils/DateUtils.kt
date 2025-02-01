package org.example.project.utils


import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun String.formatDate(): String {
    return try {
        val instant = Instant.parse(this)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val month = when(dateTime.monthNumber) {
            1 -> "Jan"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Apr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Aug"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            12 -> "Dec"
            else -> ""
        }

        val day = dateTime.dayOfMonth
        val hour = dateTime.hour.toString().padStart(2, '0')
        val minute = dateTime.minute.toString().padStart(2, '0')

        "$month $day, $hour:$minute"
    } catch (e: Exception) {
        this // Retorna el string original si hay error
    }
}

fun String.formatDateWithYear(): String {
    try {
        val instant = Instant.parse(this)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${dateTime.month.name.substring(0,3)} ${dateTime.dayOfMonth}, ${dateTime.year}"
    } catch (e: Exception) {
        return this
    }
}

fun String.formatRelativeDate(): String {
    try {
        val instant = Instant.parse(this)
        val now = Clock.System.now()
        val diff = now - instant

        return when {
            diff.inWholeMinutes < 1 -> "Just now"
            diff.inWholeMinutes < 60 -> "${diff.inWholeMinutes}m ago"
            diff.inWholeHours < 24 -> "${diff.inWholeHours}h ago"
            diff.inWholeDays < 30 -> "${diff.inWholeDays}d ago"
            else -> formatDate()
        }
    } catch (e: Exception) {
        return this
    }
}