package com.ramyres.tripplannerbr.data

import androidx.room3.ColumnTypeConverter
import java.util.Date

object Converters {
    @ColumnTypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @ColumnTypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}