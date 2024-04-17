package com.dolanj7.lunchilicious.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_order")
data class FoodOrder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val totalCost: Double
)