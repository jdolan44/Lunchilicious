package com.dolanj7.lunchilicious.domain

import com.dolanj7.lunchilicious.data.entity.FoodOrder
import com.dolanj7.lunchilicious.data.entity.LineItem
import com.dolanj7.lunchilicious.data.entity.*
import kotlinx.coroutines.flow.Flow

interface MenuRepository {
    fun getMenuListStream(): Flow<List<MenuItem>>
    fun getItemStream(id: Long): Flow<MenuItem?>
    suspend fun insertItem(item: MenuItem): Long
    suspend fun deleteItem(item: MenuItem)
    suspend fun updateItem(item: MenuItem)
    suspend fun insertOrder(order: FoodOrder) : Long
    suspend fun insertLineItem(item: LineItem)
}