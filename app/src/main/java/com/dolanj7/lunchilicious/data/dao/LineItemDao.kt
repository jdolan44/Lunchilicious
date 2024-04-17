package com.dolanj7.lunchilicious.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dolanj7.lunchilicious.data.entity.LineItem

@Dao
interface LineItemDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: LineItem) : Long
    @Update
    suspend fun update(item: LineItem)
    @Delete
    suspend fun delete(item: LineItem)
    @Query("Delete from line_item")
    suspend fun deleteAll()
}