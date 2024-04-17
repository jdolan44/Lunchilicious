package com.dolanj7.lunchilicious.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dolanj7.lunchilicious.data.entity.MenuItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuItemDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MenuItem) : Long
    @Update
    suspend fun update(item: MenuItem)
    @Delete
    suspend fun delete(item: MenuItem)
    @Query("Delete from menu_item")
    suspend fun deleteAll()
    @Query("SELECT * from menu_item WHERE id = :id")
    fun getItem(id: Long): Flow<MenuItem>
    @Query("SELECT * from menu_item ORDER BY id ASC")
    fun getMenuList(): Flow<List<MenuItem>>
}