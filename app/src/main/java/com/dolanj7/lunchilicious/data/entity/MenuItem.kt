package com.dolanj7.lunchilicious.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_item")
data class MenuItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String,
    val name: String,
    val description: String,
    val unitPrice: Double
)