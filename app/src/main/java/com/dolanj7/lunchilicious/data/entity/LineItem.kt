package com.dolanj7.lunchilicious.data.entity

import androidx.room.Entity

@Entity(tableName = "line_item", primaryKeys = ["oid", "lineNo"])
data class LineItem(
    val oid: String,
    val lineNo: Long,
    val itemId: Long = 0,
    val quantity: Long = 1
)