package com.dolanj7.lunchilicious.data.entity

import androidx.room.Entity

@Entity(tableName = "line_item", primaryKeys = ["oid", "lineNo"])
data class LineItem(
    val oid: Long = 0,
    val lineNo: Long,
    val itemId: Long = 0
)