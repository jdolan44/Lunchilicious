package com.dolanj7.lunchilicious.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dolanj7.lunchilicious.data.entity.FoodOrder

@Dao
interface FoodOrderDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: FoodOrder) : Long
    @Update
    suspend fun update(order: FoodOrder)
    @Delete
    suspend fun delete(order: FoodOrder)
    @Query("Delete from food_order")
    suspend fun deleteAll()
}